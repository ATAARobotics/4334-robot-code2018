package ca.fourthreethreefour;

import ca.fourthreethreefour.commands.RampRetract;
import ca.fourthreethreefour.commands.SolenoidLeft;
import ca.fourthreethreefour.commands.SolenoidRight;
import edu.first.command.Command;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.joysticks.BindingJoystick.DualAxisBind;
import edu.first.module.joysticks.BindingJoystick.WhilePressed;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;
import edu.wpi.first.wpilibj.DriverStation;

public class Robot extends IterativeRobotAdapter {

	/*
	 * Creates Subsystems AUTO and TELEOP to separate modules required to be enabled
	 * in autonomous and modules required to be enabled in teleoperated mode.
	 */
	private final Subsystem AUTO_MODULES = new Subsystem(new Module[] { arm, drive });

	private final Subsystem TELEOP_MODULES = new Subsystem(new Module[] { arm, drive, controllers, ramp });

	// Puts the above two subsystems into this subsystem. Subsystemception!
	private final Subsystem ALL_MODULES = new Subsystem(new Module[] { AUTO_MODULES, TELEOP_MODULES });

	/* 
	 * The current instance of the driver station. Needed in order to send and
	 * receive information (not controller inputs) from the driver station.
	 */
	DriverStation ds = DriverStation.getInstance();

	/*
	 * Constructor for the custom Robot class. Needed because IterativeRobotAdapter requires a string for some reason.
	 * TODO Name the robot!
	 */
	public Robot() {
		super("ATA 2018");
	}

	private WhilePressed leftRampRetractionBind, rightRampRetractionBind;

	// runs when the robot is first turned on
	@Override
	public void init() {
		// Initalizes all modules
		ALL_MODULES.init();

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

		// Creates an axis bind for the left and right sticks
		controller1.addAxisBind(new DualAxisBind(controller1.getLeftDistanceFromMiddle(), controller1.getRightX()) {
			@Override
			public void doBind(double speed, double turn) {
				if (turn == 0 && speed == 0) {
					drivetrain.stopMotor();
				} else {
					drivetrain.arcadeDrive(speed, turn);
				}
			}

		});

		// Creates a bind to be used, with button and command RampRetract
		leftRampRetractionBind = new WhilePressed(controller1.getX(), new RampRetract(leftRamp));
		rightRampRetractionBind = new WhilePressed(controller1.getY(), new RampRetract(rightRamp));

		/*
		 * When X/Y is pressed first time, set respective Release solenoid to true
		 * (active), and create a respective RampRetractionBind. Next time pressed, runs
		 * bind.
		 */
		controller1.addWhenPressed(XboxController.X, new Command() {
			@Override
			public void run() {
				leftRelease.setPosition(true);
				controller1.addBind(leftRampRetractionBind);
			}
		});

		controller1.addWhenPressed(XboxController.Y, new Command() {
			@Override
			public void run() {
				rightRelease.setPosition(true);
				controller1.addBind(rightRampRetractionBind);
			}
		});

		// TODO Set to right direction
		// TODO LEFT_BUMPER LOW_GEAR, RIGHT_BUMPER HIGH_GEAR
		// When the LEFT_BUMPER is pressed, changes the solenoid state to low
		// When the RIGHT_BUMPER is pressed, changes the solenoid state to high
		controller1.addWhenPressed(XboxController.LEFT_BUMPER, new SolenoidLeft(gearShifter));
		controller1.addWhenPressed(XboxController.RIGHT_BUMPER, new SolenoidRight(gearShifter));

		/*
		 * Controller 2/Operator
		 */

		// TODO Set to right direction
		// TODO LEFT_BUMPER close, RIGHT_BUMBER open
		// When left bumper is pressed, it closes the grabSolenoid
		// When right bumper is pressed, it opens the grabSolenoid
		controller2.addWhenPressed(XboxController.LEFT_BUMPER, new SolenoidLeft(grabSolenoid));
		controller2.addWhenPressed(XboxController.RIGHT_BUMPER, new SolenoidRight(grabSolenoid));

		// TODO Set to right direction
		// TODO B Extend, A Retract
		// When the B button is pressed, it extends the armSolenoid
		// When the A button is pressed, it retracts the armSolenoid
		controller2.addWhenPressed(XboxController.B, new SolenoidLeft(armSolenoid));
		controller2.addWhenPressed(XboxController.A, new SolenoidRight(armSolenoid));

		// Sets a deadband to prevent input less than 0.1
		controller2.addDeadband(XboxController.LEFT_TRIGGER, 0.1);
		controller2.addDeadband(XboxController.RIGHT_TRIGGER, 0.1);
		// Inverts the axis of the left_trigger
		controller2.invertAxis(XboxController.LEFT_TRIGGER);
		// Binds the axis to the motor
		controller2.addAxisBind(XboxController.LEFT_TRIGGER, armMotor);
		controller2.addAxisBind(XboxController.RIGHT_TRIGGER, armMotor);
	}

	@Override
	public void periodicDisabled() {
		// TODO Robot should check settings file here

	}

	// Runs at the beginning of autonomous
	@Override
	public void initAutonomous() {
		AUTO_MODULES.enable();
		// Gets game-specific information (switch and scale orientations) from FMS.
		// TODO use this in selecting autonomous routine
		ds.getGameSpecificMessage();
		drivetrain.setSafetyEnabled(false); // WE DON'T NEED SAFETY
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
		 * If any of these solenoids are are in the OFF position, set them to a default position.
		 * Necessary because most of our code for operating solenoids reverses them,
		 * which cannot be done for solenoids in the OFF position.
		 * TODO check which positions these solenoids should be set to when initiating teleop
		 */
		if (grabSolenoid.get() == Direction.OFF) {
			grabSolenoid.set(GRAB_OPEN);
		}
		if (armSolenoid.get() == Direction.OFF) {
			armSolenoid.set(ARM_EXTEND);
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
	}

	// Runs at the end of teleop
	@Override
	public void endTeleoperated() {
		controller1.removeBind(leftRampRetractionBind);
		controller1.removeBind(rightRampRetractionBind);
		TELEOP_MODULES.disable();
	}

}
