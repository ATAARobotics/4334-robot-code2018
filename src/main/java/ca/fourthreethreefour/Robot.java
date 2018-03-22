package main.java.ca.fourthreethreefour;

import java.io.File;
import java.io.IOException;

import edu.first.command.Command;
import edu.first.command.Commands;
import edu.first.commands.common.SetOutput;
import edu.first.identifiers.Output;
import edu.first.module.Module;
import edu.first.module.joysticks.BindingJoystick.DualAxisBind;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import main.java.ca.fourthreethreefour.commands.ReverseSolenoid;
import main.java.ca.fourthreethreefour.commands.debug.Logging;
import main.java.ca.fourthreethreefour.settings.AutoFile;
import main.java.ca.fourthreethreefour.subsystems.RotationalArm;


public class Robot extends IterativeRobotAdapter implements Constants {

	/*
	 * Creates Subsystems AUTO and TELEOP to separate modules required to be enabled
	 * in autonomous and modules required to be enabled in teleoperated mode, 
	 * then puts the two subsystems into ALL_MODULES subsystem. Subsystemception!
	 */
	private final Subsystem 
		AUTO_MODULES = new Subsystem(new Module[] { arm, drive, encoders }),
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

	// runs when the robot is first turned on
	@Override
	public void init() {
		// Initializes all modules
		ALL_MODULES.init();

		distancePID.setTolerance(DISTANCE_TOLERANCE);
		turnPID.setTolerance(TURN_TOLERANCE);

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
		controller2.addDeadband(XboxController.RIGHT_FROM_MIDDLE, 0.12);
		controller2.changeAxis(XboxController.RIGHT_FROM_MIDDLE, speedFunction);
		controller2.invertAxis(XboxController.RIGHT_FROM_MIDDLE);
		controller2.addAxisBind(controller2.getRightDistanceFromMiddle(), leftIntake);
		controller2.addAxisBind(controller2.getRightDistanceFromMiddle(), rightIntake);

		//TODO Up scale, sides switch, down ground
		
		// When left bumper is pressed, it closes the clawSolenoid
		// When right bumper is pressed, it opens the clawSolenoid
		controller2.addWhenPressed(XboxController.RIGHT_BUMPER, new ReverseSolenoid(clawSolenoid));

		// When the A button is pressed, it extends the flexSolenoid
		// When the B button is pressed, it retracts the flexSolenoid
		controller2.addWhenPressed(XboxController.LEFT_BUMPER, new ReverseSolenoid(flexSolenoid));

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
		controller2.addWhenPressed(XboxController.DPAD_RIGHT, new SetOutput(RotationalArm.armPID, ARM_PID_MEDIUM));

		controller2.addWhenPressed(XboxController.DPAD_UP, RotationalArm.armPID.enableCommand());
		controller2.addWhenPressed(XboxController.DPAD_UP, new SetOutput(RotationalArm.armPID, ARM_PID_HIGH));
	}

	private Command // Declares these as Command
		commandQualsLeft,
		commandQualsRight,
		commandPlayoffsRight,
		commandPlayoffsLeft,
		commandTwoCube,
		commandTest;

	@Override
	public void initDisabled() {
		ALL_MODULES.disable();
		RotationalArm.armPID.disable();
		potentiometer.enable();
	}

	@Override
	public void periodicDisabled() {
		Logging.logf("Potentiometer value: (abs: %.2f) (rel: %.2f)", potentiometer.get(), ARM_PID_TOP - potentiometer.get());
		Timer.delay(0.25);
		
		try {
			settingsFile.reload();
		} catch (NullPointerException e) {
			Timer.delay(0.25);
		}

		// TODO add limit switch button to set ARM_PID_TOP constant to current potentiometer value
		
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
			try { // Creates a new AutoFile with the file of each game, and makes it a command.
				commandQualsLeft = new AutoFile(new File("qualsLeft" + AUTO_TYPE + ".txt")).toCommand();
				commandQualsRight = new AutoFile(new File("qualsRight" + AUTO_TYPE + ".txt")).toCommand();
			} catch (IOException e) {
				throw new Error(e.getMessage());
			}
			
			// playoff autos are optional
			try {
				commandPlayoffsRight = new AutoFile(new File("playoffsRight" + AUTO_TYPE + ".txt")).toCommand();
			} catch (IOException e) {
				commandPlayoffsRight = null;
			}
			
			try {
				commandPlayoffsLeft = new AutoFile(new File("playoffsLeft" + AUTO_TYPE + ".txt")).toCommand();
			} catch (IOException e) {
				commandPlayoffsLeft = null;
			}

			try {
				commandTwoCube = new AutoFile(new File("twocube.txt")).toCommand();
			} catch (IOException e) {
				commandTwoCube = null;
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
				if (DriverStation.getInstance().getMatchType() == DriverStation.MatchType.Elimination || IS_PLAYOFF) {
					if (gameData.charAt(1) == 'R' && gameData.charAt(0) == 'R' && commandTwoCube != null) {
						// if our side of the scale is on the right
						Commands.run(commandTwoCube);
					} else if (gameData.charAt(1) == 'R' && commandPlayoffsRight != null) {
						// if our side of the scale is on the right
						Commands.run(commandPlayoffsRight);
					} else if (gameData.charAt(1) == 'L' && commandPlayoffsLeft != null) {
						// if our side of the scale is on the left
						Commands.run(commandPlayoffsLeft);
					} else if (gameData.charAt(0) == 'R') {
						// if our side of the switch is on the right
						Commands.run(commandQualsRight);
					} else if (gameData.charAt(0) == 'L') {
						// if our side of the switch is on the left
						Commands.run(commandQualsLeft);
					}
				}
			}
		}
	}

	@Override
	public void periodicAutonomous() {
	}
	// Runs at the end of autonomous
	@Override
	public void endAutonomous() {
		AUTO_MODULES.disable();
	}

	// Runs at the beginning of teleop
	@Override
	public void initTeleoperated() {
		TELEOP_MODULES.enable();
		drivetrain.setSafetyEnabled(true); // Maybe we do...
		
		flexSolenoid.set(FLEX_RETRACT);
		clawSolenoid.set(CLAW_CLOSE);
		gearShifter.set(LOW_GEAR);
	}

	// Runs every (approx.) 20ms in teleop
	@Override
	public void periodicTeleoperated() {
		// Performs the binds set in init()
		controller1.doBinds();
		controller2.doBinds();
	}

	// Runs at the end of teleop
	@Override
	public void endTeleoperated() {
		RotationalArm.armPID.disable();
		TELEOP_MODULES.disable();
	}

}
