package ca.fourthreethreefour;

import ca.fourthreethreefour.commands.ReverseSolenoid;
import ca.fourthreethreefour.commands.SolenoidLeft;
import ca.fourthreethreefour.commands.SolenoidRight;
import edu.first.command.Command;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.joysticks.BindingJoystick.DualAxisBind;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;

public class Robot extends IterativeRobotAdapter {
	
	/*
	 * Creates Subsystems AUTO and TELEOP to separate modules required to be enabled in autonomous and 
	 * modules required to be enabled in teleoperated mode.
	 */
	private final Subsystem AUTO_MODULES = new Subsystem(
			new Module[] { 	arm, drive });
	
	private final Subsystem TELEOP_MODULES = new Subsystem(
			new Module[] { arm, drive, controllers, ramp });
	
	//Puts the above two subsystems into this subsystem. Subsystemception
	private final Subsystem ALL_MODULES =  new Subsystem(new Module[] { AUTO_MODULES, TELEOP_MODULES });
	
	/*
	 * Constructor for the custom Robot class. Needed because IterativeRobotAdapter 
	 * requires a string for some reason. 
	 * TODO Name the robot!
	 */
	public Robot() {
		super("ATA 2018");
	}
	
	//initializes robot
	@Override
	public void init() {
		// Initalizes all modules
		ALL_MODULES.init();
		
		// Controller 1/driver
		/*
		 *  Sets the deadband for LEFT_FROM_MIDDLE and RIGHT_X. If the input value from either of those 
		 *  axes does not exceed the deadband, the value will be set to zero.
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
		
		/* 
		 *  While X is pressed, run the rampTrain with speed RAMP_RELEASE_SPEED and no value for rotation.
		 *
		 *  Creates an anonymous class of type Command and specifies what it does when the run() function
		 *  is called.
		 */
		controller1.addWhilePressed(XboxController.X, new Command() {
			@Override
			public void run() {
				//takes RAMP_RELEASE_SPEED, specified in settings.txt, as the speed for motor
				rampTrain.arcadeDrive(RAMP_RELEASE_SPEED, 0); 
				}
			});
		
		/* 
		 *  While Y is pressed, run the rampTrain using arcadeDrive with speed RAMP_RETRACT_SPEED and no 
		 *  value for rotation.
		 */
		controller1.addWhilePressed(XboxController.Y, new Command() {
			@Override
			public void run() {
				//takes RAMP_RETRACT_SPEED, specified in settings.txt, as the speed for motor 
				rampTrain.arcadeDrive(RAMP_RETRACT_SPEED, 0); 
				}
			});
        
		// Controller 2/operator
		// When left bumper is pressed, it reverses the grabSolenoid
		controller2.addWhenPressed(XboxController.LEFT_BUMPER, new ReverseSolenoid(grabSolenoid));
		
		// When right bumper is pressed, it reverses the armSolenoid
		controller2.addWhenPressed(XboxController.RIGHT_BUMPER, new ReverseSolenoid(armSolenoid));
		
		//When the A button is pressed, changes the solenoid state to left
		//When the B button is pressed, changes the solenoid state to right
		controller2.addWhenPressed(XboxController.A, new SolenoidLeft(motorSolenoid)); 
		controller2.addWhenPressed(XboxController.B, new SolenoidRight(motorSolenoid)); 

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
		//TODO Robot should check settings file here
		
    	}
	
	// Runs at the beginning of autonomous
	@Override
	public void initAutonomous() { 
		AUTO_MODULES.enable();
		drivetrain.setSafetyEnabled(false); // WE DONT NEED SAFETY
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
		 * Necessary because most of our code for the solenoids reverses them, which cannot be done
		 * for solenoids in the OFF position.
		 * TODO check the default positions for these
		 */
		if (grabSolenoid.get() == Direction.OFF) {
			grabSolenoid.set(DualActionSolenoid.Direction.RIGHT);
		}
		if (armSolenoid.get() == Direction.OFF) {
			armSolenoid.set(DualActionSolenoid.Direction.RIGHT);
		}
		if (motorSolenoid.get() == Direction.OFF) {
			motorSolenoid.set(DualActionSolenoid.Direction.LEFT);
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
    	TELEOP_MODULES.disable();
   	}

}

