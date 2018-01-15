package ca.fourthreethreefour;

import ca.fourthreethreefour.commands.ReverseSolenoid;
import edu.first.command.Command;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.actuators.SpeedController;
import edu.first.module.joysticks.XboxController;
import edu.first.module.joysticks.BindingJoystick.DualAxisBind;
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
			new Module[] { arm, drive, controllers });
	
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
		//Initalizes all modules
		ALL_MODULES.init();
		//TODO add controls
		
		//Controller 1/driver
		
		//set minimum distance from middle
		controller1.addDeadband(XboxController.LEFT_FROM_MIDDLE, 0.20);
		controller1.changeAxis(XboxController.LEFT_FROM_MIDDLE, speedFunction);
		
		//set minimum distance from middle
		controller1.addDeadband(XboxController.RIGHT_X, 0.20);
		controller1.invertAxis(XboxController.RIGHT_X);
		
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
		
		//Controller 2/operator
		
		controller2.addDeadband(XboxController.LEFT_TRIGGER, 0.1);
		controller2.addDeadband(XboxController.RIGHT_TRIGGER, 0.1);
		
		controller2.addWhenPressed(XboxController.LEFT_BUMPER, new ReverseSolenoid(grabSolenoid));
		controller2.addWhenPressed(XboxController.RIGHT_BUMPER, new ReverseSolenoid(armSolenoid));
		//controller2.addWhenPressed(XboxController.A, highGear); //need something to change to high gear
		//controller2.addWhenPressed(XboxController.B, lowGear); //need something to change to high gear
		
		controller2.addAxisBind(XboxController.LEFT_TRIGGER, armMotor);
		controller2.addAxisBind(XboxController.RIGHT_TRIGGER, armMotor);
		controller2.invertAxis(XboxController.LEFT_TRIGGER);
	}
	
	@Override
	public void periodicDisabled() {
		//TODO Robot should check settings file here
	}
	
	@Override
	public void initAutonomous() {
		AUTO_MODULES.enable();
	}
	
	@Override
	public void initTeleoperated() {
		TELEOP_MODULES.enable();
		if (grabSolenoid.get() == Direction.OFF) { //if it off, set it right
			grabSolenoid.set(DualActionSolenoid.Direction.RIGHT);
		}
		if (armSolenoid.get() == Direction.OFF) { //if it off, set it right
			armSolenoid.set(DualActionSolenoid.Direction.RIGHT);
		}
		if (motorSolenoid.get() == Direction.OFF) { //if it off, set it left
			motorSolenoid.set(DualActionSolenoid.Direction.LEFT);
		}
	}
	
	@Override
	public void periodicTeleoperated() {
		controller1.doBinds();
		controller2.doBinds();
	}

}

