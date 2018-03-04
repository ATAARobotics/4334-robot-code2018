package main.java.ca.fourthreethreefour;

import java.io.File;
import java.io.IOException;

import edu.first.command.Command;
import edu.first.command.Commands;
import edu.first.commands.common.SetOutput;
import edu.first.identifiers.Output;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.joysticks.BindingJoystick.DualAxisBind;
import edu.first.module.joysticks.BindingJoystick.WhilePressed;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;
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
		TELEOP_MODULES = new Subsystem(new Module[] { arm, drive, controllers, ramp }),
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

	// Creates a bind to be used, with button and command RampRetract
	private WhilePressed 
		leftRampRetractionBind = new WhilePressed(controller2.getBack(), new SetOutput(leftRamp, RAMP_RETRACT_SPEED)),
		rightRampRetractionBind = new WhilePressed(controller2.getStart(), new SetOutput(rightRamp, RAMP_RETRACT_SPEED));

	String settingsActive = settingsFile.toString();

	// runs when the robot is first turned on
	@Override
	public void init() {
		// Initializes all modules
		ALL_MODULES.init();

		// Initializes the CameraServer twice. That's how it's done
        //CameraServer.getInstance().startAutomaticCapture();
        //CameraServer.getInstance().startAutomaticCapture();
		
		// Controller 1/driver
		/*
		 * Sets the deadband for LEFT_FROM_MIDDLE and RIGHT_X. If the input value from
		 * either of those axes does not exceed the deadband, the value will be set to
		 * zero.
		 */
		controller1.addDeadband(XboxController.LEFT_FROM_MIDDLE, 0.20);
		controller1.changeAxis(XboxController.LEFT_FROM_MIDDLE, speedFunction);
		controller1.addDeadband(XboxController.RIGHT_X, 0.20);
		controller1.invertAxis(XboxController.RIGHT_X);
		controller1.changeAxis(XboxController.RIGHT_X, turnFunction);
		controller1.addDeadband(XboxController.TRIGGERS, 0.20);

		// Creates an axis bind for the left and right sticks
		controller1.addAxisBind(new DualAxisBind(controller1.getLeftDistanceFromMiddle(), controller1.getRightX()) {
			@Override
			public void doBind(double speed, double turn) {
				drivetrain.arcadeDrive(speed, turn);
			}
		});

		// When right stick is pressed, reverses gearShifter, changing the gear.
		controller1.addWhenPressed(XboxController.RIGHT_STICK, new ReverseSolenoid(gearShifter));

		/*
		 * Controller 2/Operator
		 */

		controller2.changeAxis(XboxController.TRIGGERS, armFunction);

		controller2.addBind(rightRampRetractionBind);
		controller2.addBind(leftRampRetractionBind);

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
					RotationalArm.armMotor.set(v);
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
		commandLRL,
		commandRLR,
		commandLLL,
		commandRRR;

	@Override
	public void initDisabled() {
		ALL_MODULES.disable();
		RotationalArm.armPID.disable();
	}

	@Override
	public void periodicDisabled() {
		
		try {
			settingsFile.reload();
		} catch (NullPointerException e) {
			Timer.delay(1);
		}

		// TODO add limit switch button to set ARM_PID_TOP constant to current potentiometer value
		
		if (!settingsActive.equalsIgnoreCase(settingsFile.toString())) {
			throw new RuntimeException(); // If it HAS changed, best to crash the Robot so it gets the update.
		}
		
		if (AUTO_TYPE == "") { // If no type specified, ends method.
			return;
		}
		
		try { // Creates a new AutoFile with the file of each game, and makes it a command.
			commandLRL = new AutoFile(new File("LRL" + AUTO_TYPE + ".txt")).toCommand();
			commandRLR = new AutoFile(new File("RLR" + AUTO_TYPE + ".txt")).toCommand();
			commandLLL = new AutoFile(new File("LLL" + AUTO_TYPE + ".txt")).toCommand();
			commandRRR = new AutoFile(new File("RRR" + AUTO_TYPE + ".txt")).toCommand();
		} catch (IOException e) {
			throw new Error(e.getMessage());
		}

		Timer.delay(1);
	}

	// Runs at the beginning of autonomous
	@Override
	public void initAutonomous() {
		AUTO_MODULES.enable();
		// Gets game-specific information (switch and scale orientations) from FMS.
		String gameData = ds.getGameSpecificMessage().toUpperCase();
		drivetrain.setSafetyEnabled(false); // WE DON'T NEED SAFETY
		if (gameData.length() > 0) {
			switch (gameData) {
			case "LRL":
				Commands.run(commandLRL);
				break;
			case "RLR":
				Commands.run(commandRLR);
				break;
			case "LLL":
				Commands.run(commandLLL);
				break;
			case "RRR":
				Commands.run(commandRRR);
				break;
			}
		}
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
		/*
		 * If any of these solenoids are are in the OFF position, set them to a default
		 * position. Necessary because most of our code for operating solenoids requires
		 * them to not be in the OFF position.
		 */
		if (clawSolenoid.get() == Direction.OFF) {
			clawSolenoid.set(CLAW_OPEN);
		}
		if (flexSolenoid.get() == Direction.OFF) {
			flexSolenoid.set(FLEX_RETRACT);
		}
		if (gearShifter.get() == Direction.OFF) {
			gearShifter.set(LOW_GEAR);
		}
		
	}

	// Runs every (approx.) 20ms in teleop
	@Override
	public void periodicTeleoperated() {
		// Performs the binds set in init()
		controller1.doBinds();
		controller2.doBinds();

        if (RotationalArm.shouldArmBeFlexed()) { flexSolenoid.set(FLEX_RETRACT); }

		Logging.log("potentiometer: " + (ARM_PID_TOP - potentiometer.get()));
	}
	

	// Runs at the end of teleop
	@Override
	public void endTeleoperated() {
		controller1.removeBind(leftRampRetractionBind);
		controller1.removeBind(rightRampRetractionBind);
		RotationalArm.armPID.disable();
		TELEOP_MODULES.disable();
	}

}
