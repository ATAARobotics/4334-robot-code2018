package ca.fourthreethreefour;

import java.io.File;
import java.io.IOException;

import ca.fourthreethreefour.commands.RampRetract;
import ca.fourthreethreefour.commands.ReverseSolenoid;
import ca.fourthreethreefour.commands.SetSolenoid;
import ca.fourthreethreefour.settings.AutoFile;
import edu.first.command.Command;
import edu.first.command.Commands;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.joysticks.BindingJoystick.DualAxisBind;
import edu.first.module.joysticks.BindingJoystick.WhilePressed;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobotAdapter {

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
		leftRampRetractionBind = new WhilePressed(controller1.getBack(), new RampRetract(leftRamp)),
		rightRampRetractionBind = new WhilePressed(controller1.getStart(), new RampRetract(rightRamp));

	// runs when the robot is first turned on
	@Override
	public void init() {
		// Initalizes all modules
		ALL_MODULES.init();
		
		// Initializes the CameraServer twice. That's how it's done
        CameraServer.getInstance().startAutomaticCapture();
        CameraServer.getInstance().startAutomaticCapture();

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
				drivetrain.arcadeDrive(speed, turn);
			}
		});

		// When A is pressed, reverses gearShifter, changing the gear.
		controller1.addWhenPressed(XboxController.A, new ReverseSolenoid(gearShifter));

		/*
		 * Controller 2/Operator
		 */
		
		/*
		 * When Start/Back is pressed first time, set respective Release solenoid to true
		 * (active), and create a respective RampRetractionBind. Next time pressed, runs
		 * bind.
		 */
		controller2.addWhenPressed(XboxController.START, new Command() {
			@Override
			public void run() {
				rightRelease.setPosition(true);
				controller2.addBind(rightRampRetractionBind);
			}
		});

		controller2.addWhenPressed(XboxController.BACK, new Command() {
			@Override
			public void run() {
				leftRelease.setPosition(true);
				controller2.addBind(leftRampRetractionBind);
			}
		});

		// When left bumper is pressed, it closes the clawSolenoid
		// When right bumper is pressed, it opens the clawSolenoid
		controller2.addWhenPressed(XboxController.LEFT_BUMPER, new SetSolenoid(clawSolenoid, CLAW_CLOSE));
		controller2.addWhenPressed(XboxController.RIGHT_BUMPER, new SetSolenoid(clawSolenoid, CLAW_OPEN));

		// When the A button is pressed, it extends the flexSolenoid
		// When the B button is pressed, it retracts the flexSolenoid
		controller2.addWhenPressed(XboxController.A, new SetSolenoid(flexSolenoid, FLEX_EXTEND));
		controller2.addWhenPressed(XboxController.B, new SetSolenoid(flexSolenoid, FLEX_RETRACT));

		// Binds the axis to the motor
		controller2.addAxisBind(XboxController.TRIGGERS, rotationalArm);
	}

	private Command // Declares these as Command
		commandLRL,
		commandRLR,
		commandLLL,
		commandRRR;

	@Override
	public void periodicDisabled() {
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
		
		double armAngle = rotationalArm.getAnalog();
		if (armAngle >= ARM_ANGLE_MIN && armAngle <= ARM_ANGLE_MAX) {
			flexSolenoid.set(FLEX_RETRACT);
		}
	}

	// Runs at the end of teleop
	@Override
	public void endTeleoperated() {
		controller1.removeBind(leftRampRetractionBind);
		controller1.removeBind(rightRampRetractionBind);
		TELEOP_MODULES.disable();
	}

}
