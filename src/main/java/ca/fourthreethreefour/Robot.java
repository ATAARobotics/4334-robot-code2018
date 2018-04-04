package main.java.ca.fourthreethreefour;

import java.io.File;
import java.io.IOException;

import edu.first.command.Command;
import edu.first.command.Commands;
import edu.first.commands.common.SetOutput;
import edu.first.identifiers.Output;
import edu.first.lang.OutOfSyncException;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.joysticks.BindingJoystick.DualAxisBind;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.MatchType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import main.java.ca.fourthreethreefour.commands.ReverseSolenoid;
import main.java.ca.fourthreethreefour.commands.debug.Logging;
import main.java.ca.fourthreethreefour.settings.AutoFile;
import main.java.ca.fourthreethreefour.subsystems.Intake;
import main.java.ca.fourthreethreefour.subsystems.RotationalArm;


public class Robot extends IterativeRobotAdapter implements Constants {

	/*
	 * Creates Subsystems AUTO and TELEOP to separate modules required to be enabled
	 * in autonomous and modules required to be enabled in teleoperated mode, 
	 * then puts the two subsystems into ALL_MODULES subsystem. Subsystemception!
	 */
	private final Subsystem 
		AUTO_MODULES = new Subsystem(new Module[] { arm, drive, encoders, intake }),
		TELEOP_MODULES = new Subsystem(new Module[] { arm, drive, controllers, intake }),
		ALL_MODULES = new Subsystem(new Module[] { AUTO_MODULES, TELEOP_MODULES });

	/*
	 * The current instance of the driver station. Needed in order to send and
	 * receive information (not controller inputs) from the driver station.
	 */
	DriverStation ds = DriverStation.getInstance();

	/*
	 * Constructor for the custom Robot class. Needed because IterativeRobotAdapter
	 * requires a string for some reason. TODO Name the robot!
	 */
	public Robot() {
		super("ATA 2018");
	}

	String settingsActive = settingsFile.toString();
	boolean intakeActive = false;

	// runs when the robot is first turned on
	@Override
	public void init() {
		// Initializes all modules
		ALL_MODULES.init();

		distancePID.setTolerance(DISTANCE_TOLERANCE);
		turnPID.setTolerance(TURN_TOLERANCE);
		intakePID.setTolerance(INTAKE_TOLERANCE);

		// Initializes the CameraServer twice. That's how it's done
        CameraServer.getInstance().startAutomaticCapture();
        //CameraServer.getInstance().startAutomaticCapture();
        
		// Controller 1/driver
		/*
		 * Sets the deadband for LEFT_FROM_MIDDLE and RIGHT_X. If the input value from
		 * either of those axes does not exceed the deadband, the value will be set to
		 * zero.
		 */
		controller1.addDeadband(XboxController.LEFT_FROM_MIDDLE, 0.12);
		controller1.changeAxis(XboxController.LEFT_FROM_MIDDLE, speedFunction);
		controller1.addDeadband(XboxController.RIGHT_X, 0.12);
		controller1.invertAxis(XboxController.RIGHT_X);
		controller1.changeAxis(XboxController.RIGHT_X, turnFunction);
		controller1.addDeadband(XboxController.TRIGGERS, 0.15);

		// Creates an axis bind for the left and right sticks
		controller1.addAxisBind(new DualAxisBind(controller1.getLeftDistanceFromMiddle(), controller1.getRightX()) {
			@Override
			public void doBind(double speed, double turn) {
                turn += (speed > 0) ? DRIVE_COMPENSATION : (speed < 0) ? -DRIVE_COMPENSATION : 0;
				drivetrain.arcadeDrive(speed, turn);
				if(Math.abs(speed) < LOW_GEAR_THRESHOLD) {
					gearShifter.set(LOW_GEAR);
				}
			}
		});

		// When right stick is pressed, reverses gearShifter, changing the gear.
		controller1.addWhenPressed(XboxController.RIGHT_STICK, new ReverseSolenoid(gearShifter));

		/*
		 * Controller 2/Operator
		 */
		
		controller2.changeAxis(XboxController.TRIGGERS, armFunction);

		// Binds the Intake motors to the Right stick
		controller1.addDeadband(XboxController.TRIGGERS, 0.12);
		controller1.changeAxis(XboxController.TRIGGERS, intakeFunction);
		controller1.invertAxis(XboxController.TRIGGERS);
		controller1.addAxisBind(controller1.getTriggers(), rightIntake);
		//controller2.addAxisBind(controller2.getRightDistanceFromMiddle(), rightIntake);
		controller1.addAxisBind(controller1.getTriggers(), new Output() {
			@Override
			public void set(double value) {
				leftIntake.set(-value);
			}
		});
		controller2.addDeadband(XboxController.LEFT_FROM_MIDDLE, 0.12);
		controller2.changeAxis(XboxController.LEFT_FROM_MIDDLE, intakeArmFunction);
		controller2.invertAxis(XboxController.LEFT_FROM_MIDDLE);
		controller2.addAxisBind(controller2.getLeftDistanceFromMiddle(), new Output() {
			@Override
			public void set(double v) {
				if (Math.abs(v) > 0.2) {
					if (intakePID.isEnabled()) {
						intakePID.disable();
					}
				}
				if (!intakePID.isEnabled()) {
					armIntake.set(v);
				}
			}
		});
		
		// When left bumper is pressed, it closes the clawSolenoid
		// When right bumper is pressed, it opens the clawSolenoid
		controller2.addWhenPressed(XboxController.RIGHT_BUMPER, new ReverseSolenoid(clawSolenoid));

		// When the A button is pressed, it extends the flexSolenoid
		// When the B button is pressed, it retracts the flexSolenoid
		controller2.addWhenPressed(XboxController.LEFT_BUMPER, new ReverseSolenoid(flexSolenoid));
		controller2.addWhenPressed(XboxController.LEFT_BUMPER, new Command() {
			@Override
			public void run() {
				double armAngle = ARM_PID_TOP - armPotentiometer.get();
				if (flexSolenoid.get() == FLEX_EXTEND && clawSolenoid.get() == CLAW_CLOSE) {
					if (armAngle >= INTAKE_ANGLE_MIN && armAngle <= INTAKE_ANGLE_MAX) {
						int i = 0;
						while (i < INTAKE_RELEASE_LENGTH) {
							Logging.log("Length: " + i);
							Logging.log("Intake Running");
							leftIntake.set(-INTAKE_RELEASE_SPEED);
							rightIntake.set(INTAKE_RELEASE_SPEED);
							i++;
						}
					}
				}
			}
		});

		// Binds the axis to the motor
		controller2.addAxisBind(XboxController.TRIGGERS, new Output() {
			@Override
			public void set(double v) { // TODO this should set setpoint instead of disabling PID
				if (Math.abs(v) > 0.2) {
					if (RotationalArm.armPID.isEnabled()) {
						RotationalArm.armPID.disable();
					}
				}
				if (!RotationalArm.armPID.isEnabled()) {
					RotationalArm.output.set(-v);
				}
			}
		});
		
		controller2.addWhenPressed(XboxController.DPAD_DOWN, RotationalArm.armPID.enableCommand());
		controller2.addWhenPressed(XboxController.DPAD_DOWN, new SetOutput(RotationalArm.armPID, ARM_PID_LOW));
		
		controller2.addWhenPressed(XboxController.DPAD_RIGHT, RotationalArm.armPID.enableCommand());
		controller2.addWhenPressed(XboxController.DPAD_RIGHT, new SetOutput(RotationalArm.armPID, ARM_PID_MIDDLE));

		controller2.addWhenPressed(XboxController.DPAD_UP, RotationalArm.armPID.enableCommand());
		controller2.addWhenPressed(XboxController.DPAD_UP, new SetOutput(RotationalArm.armPID, ARM_PID_HIGH));
		
		controller1.addWhenPressed(XboxController.A, new Command() {
			
			@Override
			public void run() {
				if (intakeActive) {
					intakeActive = false;
				} else {
					intakeActive = true;
				}
			}
		});

		controller2.addWhenPressed(XboxController.A, intakePID.enableCommand());
		controller2.addWhenPressed(XboxController.A, new SetOutput(intakePID, INTAKE_PID_GROUND));

		controller2.addWhenPressed(XboxController.Y, intakePID.enableCommand());
		controller2.addWhenPressed(XboxController.Y, new SetOutput(intakePID, INTAKE_PID_SHOOTING));
	}

	private Command // Declares these as Command
		commandTwoCube,
		commandRRRScale,
		commandRRRSwitch,
		commandLLLSwitch,
		commandLLLScale,
		//commandRLRSwitch,
		//commandRLRScale,
		//commandLRLSwitch,
		//commandLRLScale,
		commandAutoRun,
		commandTest;

	@Override
	public void initDisabled() {
		ALL_MODULES.disable();
		RotationalArm.armPID.disable();
		armPotentiometer.enable();
		intakePotentiometer.enable();
	}

	@Override
	public void periodicDisabled() {
		Logging.logf(
				"Arm Potentiometer value: (abs: %.2f) (rel: %.2f)"
						+ " Intake Potentiometer value: (abs: %.2f) (rel: %.2f)",
				armPotentiometer.get(), ARM_PID_TOP - armPotentiometer.get(), intakePotentiometer.get(),
				INTAKE_PID_BOTTOM - intakePotentiometer.get());
		Timer.delay(0.25);
		
		try {
			settingsFile.reload();
		} catch (NullPointerException e) {
			Timer.delay(0.25);
		}

		// TODO add limit switch button to set ARM_PID_TOP constant to current
		// potentiometer value

		if (!settingsActive.equalsIgnoreCase(settingsFile.toString())) {
			throw new RuntimeException(); // If it HAS changed, best to crash the Robot so it gets the update.
		}

		if (AUTO_TYPE == "") { // If no type specified, ends method.
			return;
		}

		if (AUTO_TYPE.contains("test")) {
			try {
				commandTest = new AutoFile(new File(AUTO_TYPE + ".txt")).toCommand();
			} catch (IOException e) {
				throw new Error(e.getMessage());
			}
		} else {
			try {
				commandTwoCube = new AutoFile(new File("twocube.txt")).toCommand();
			} catch (IOException e) {
				commandTwoCube = null;
			}

			try {
				commandRRRScale = new AutoFile(new File("rrr" + AUTO_TYPE + "scale.txt")).toCommand();
			} catch (IOException e) {
				commandRRRScale = null;
			}

			try {
				commandRRRSwitch = new AutoFile(new File("rrr" + AUTO_TYPE + "switch.txt")).toCommand();
			} catch (IOException e) {
				commandRRRSwitch = null;
			}
			
			try {
				commandLLLScale = new AutoFile(new File("lll" + AUTO_TYPE + "scale.txt")).toCommand();
			} catch (IOException e) {
				commandLLLScale = null;
			}

			try {
				commandLLLSwitch = new AutoFile(new File("lll" + AUTO_TYPE + "switch.txt")).toCommand();
			} catch (IOException e) {
				commandLLLSwitch = null;
			}
			
			/*try {
				commandRLRScale = new AutoFile(new File("rlr" + AUTO_TYPE + "scale.txt")).toCommand();
			} catch (IOException e) {
				commandRLRScale = null;
			}

			try {
				commandRLRSwitch = new AutoFile(new File("rlr" + AUTO_TYPE + "switch.txt")).toCommand();
			} catch (IOException e) {
				commandRLRSwitch = null;
			}
			
			try {
				commandLRLScale = new AutoFile(new File("lrl" + AUTO_TYPE + "scale.txt")).toCommand();
			} catch (IOException e) {
				commandLRLScale = null;
			}

			try {
				commandLRLSwitch = new AutoFile(new File("lrl" + AUTO_TYPE + "switch.txt")).toCommand();
			} catch (IOException e) {
				commandLRLSwitch = null;
			}*/

			try {
				commandAutoRun = new AutoFile(new File("autorun.txt")).toCommand();
			} catch (IOException e) {
				commandAutoRun = null;
			}
		}

		Timer.delay(0.25);
	}

	// Runs at the beginning of autonomous
	@Override
	public void initAutonomous() {
		AUTO_MODULES.enable();
		
		gearShifter.set(LOW_GEAR);

		// Gets game-specific information (switch and scale orientations) from FMS.
		String gameData = ds.getGameSpecificMessage().toUpperCase();
		drivetrain.setSafetyEnabled(false); // WE DON'T NEED SAFETY
		if (AUTO_TYPE.contains("test")) {
			Commands.run(commandTest);
		} else {
			if (gameData.length() > 0) {
				switch (AUTO_TYPE.toLowerCase()) {
				case "center":
					if (gameData.charAt(0) == 'R') {
						Commands.run(commandRRRSwitch);
					} else {
						Commands.run(commandLLLSwitch);
					}
					break;
				case "right":
					if (IS_PLAYOFF || DriverStation.getInstance().getMatchType() == MatchType.Elimination) {
						if (gameData.charAt(1) == 'R') {
							Commands.run(commandRRRScale);
						} else {
							Commands.run(commandLLLScale);
						}
					} else {
						if (gameData.charAt(0) == 'R') {
							Commands.run(commandRRRSwitch);
						} else {
							Commands.run(commandLLLSwitch);
						}
					}
					break;
				case "left":
					if (IS_PLAYOFF || DriverStation.getInstance().getMatchType() == MatchType.Elimination) {
						if (gameData.charAt(1) == 'R') {
							Commands.run(commandRRRScale);
						} else {
							Commands.run(commandLLLScale);
						}
					} else {
						if (gameData.charAt(0) == 'R') {
							Commands.run(commandRRRSwitch);
						} else {
							Commands.run(commandLLLSwitch);
						}
					}
					break;
				default:
					Commands.run(commandAutoRun);
					break;
				}
			} else {
				// if there's no game-specific data
				Commands.run(commandAutoRun);
			}
		}
	}

	@Override
	public void periodicAutonomous() {
		Logging.logf("Encoder value: (left: %.2f) (right: %.2f) (encoder: %.2f)", leftEncoder.get(), rightEncoder.get(), encoderInput.get());
		Timer.delay(0.5);
	}
	// Runs at the end of autonomous
	@Override
	public void endAutonomous() {
		RotationalArm.armPID.disable();
		intakePID.disable();
		AUTO_MODULES.disable();
	}

	// Runs at the beginning of teleop
	@Override
	public void initTeleoperated() {
		TELEOP_MODULES.enable();
		drivetrain.setSafetyEnabled(true); // Maybe we do...
		
		if(flexSolenoid.get() == Direction.OFF) {
			flexSolenoid.set(FLEX_RETRACT);
		}
		if (clawSolenoid.get() == Direction.OFF) {
			clawSolenoid.set(CLAW_OPEN);
		}		
		gearShifter.set(LOW_GEAR);
		intakeActive = true;
	}

	// Runs every (approx.) 20ms in teleop
	@Override
	public void periodicTeleoperated() {
		// Performs the binds set in init()
		controller1.doBinds();
		controller2.doBinds();
		if (intakeActive) {
			double leftSpeed = 0, rightSpeed = 0;
			try {
				leftSpeed = drivetrain.getLeftSpeed();
				rightSpeed = -drivetrain.getRightSpeed();
			} catch (OutOfSyncException e) {
				Timer.delay(0.5);
			}
			//Logging.logf("Speed values: (left: %.2f) (right: %.2f)", leftSpeed, rightSpeed);
			
			
			if (leftSpeed >= rightSpeed) {
				if (leftSpeed > 0) {
					leftIntake.set(leftSpeed / 2);
					rightIntake.set(-leftSpeed / 2);
				}
			} else {
				if (rightSpeed > 0) {
					leftIntake.set(rightSpeed / 2);
					rightIntake.set(-rightSpeed / 2);
				}
			}
		}
		SmartDashboard.putNumber("Arm PID ", RotationalArm.armPID.getError());
		SmartDashboard.putNumber("Intake PID ", intakePID.getError());
		Logging.logf(
				"Arm Potentiometer value: (abs: %.2f) (rel: %.2f)"
						+ " Intake Potentiometer value: (abs: %.2f) (rel: %.2f)",
				armPotentiometer.get(), ARM_PID_TOP - armPotentiometer.get(), intakePotentiometer.get(),
				INTAKE_PID_BOTTOM - intakePotentiometer.get());
	}

	// Runs at the end of teleop
	@Override
	public void endTeleoperated() {
		RotationalArm.armPID.disable();
		intakePID.disable();
		TELEOP_MODULES.disable();
	}

}
