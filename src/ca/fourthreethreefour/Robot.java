package ca.fourthreethreefour;

import java.io.File;
import java.io.IOException;

import ca.fourthreethreefour.commands.ReverseSolenoid;
import ca.fourthreethreefour.commands.SolenoidLeft;
import ca.fourthreethreefour.commands.SolenoidRight;
import ca.fourthreethreefour.settings.AutoFile;
import edu.first.command.Command;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.joysticks.BindingJoystick.DualAxisBind;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;
import edu.wpi.first.wpilibj.Timer;

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
		
		controller1.addWhilePressed(XboxController.X, new Command() { //I have no clue how this works
			//TODO Check with drive team about this button
			@Override
			public void run() {
				rampTrain.arcadeDrive(RAMP_RELEASE_SPEED, 0); //takes the RAMP_RELEASE_SPEED as the speed for motor
				}
			});
		
		controller1.addWhilePressed(XboxController.Y, new Command() { //I have no clue how this works either
			//TODO Check with drive team about this button
			@Override
			public void run() {
				rampTrain.arcadeDrive(RAMP_RETRACT_SPEED, 0); //takes the RAMP_RETRACT_SPEED as the speed for motor
				}
			});
        //TODO add ramp controls
		
		//Controller 2/operator
		
		//When left bumper is pressed, it reverses the grabSolenoid
		controller2.addWhenPressed(XboxController.LEFT_BUMPER, new ReverseSolenoid(grabSolenoid));
		
		//When right bumper is pressed, it reverses the armSolenoid
		controller2.addWhenPressed(XboxController.RIGHT_BUMPER, new ReverseSolenoid(armSolenoid));
		
		
		controller2.addWhenPressed(XboxController.A, new SolenoidLeft(motorSolenoid)); //When pressed A, changes the solenoid to left
		controller2.addWhenPressed(XboxController.B, new SolenoidRight(motorSolenoid)); //When pressed B, changed the solenoid to right

		//Sets a deadband to prevent input less than 0.1
		controller2.addDeadband(XboxController.LEFT_TRIGGER, 0.1);
		controller2.addDeadband(XboxController.RIGHT_TRIGGER, 0.1);
		//Inverts the axis of the left_trigger
		controller2.invertAxis(XboxController.LEFT_TRIGGER);
		
		//Binds the axis to the motor
		controller2.addAxisBind(XboxController.LEFT_TRIGGER, armMotor);
		controller2.addAxisBind(XboxController.RIGHT_TRIGGER, armMotor); 
	}

    @SuppressWarnings("unused") //Because it doesn't detect the code below
	private Command autoCommand;
	
	@Override
	public void periodicDisabled() { //TODO comment this area
		//TODO Clean up useless code, as I don't know what is needed and what isn't.
		//TODO Robot should check settings file here
		if (AUTO_TYPE == "") { return; }
        String alliance = ""; /* AUTO_ALLIANCE_INDEPENDENT ? "" : (allianceSwitch.getPosition() ? "red-" : "blue-"); */
        try {
            autoCommand = new AutoFile(new File(alliance + AUTO_TYPE + ".txt")).toCommand();
        } catch (IOException e) {
            // try alliance independent as backup
            try {
                autoCommand = new AutoFile(new File(AUTO_TYPE + ".txt")).toCommand();
            } catch (IOException i) {
                throw new Error(e.getMessage());
            }
        }
        
        Timer.delay(1);
    }
	
	@Override
	public void initAutonomous() { //when Autonomous is initialized
		AUTO_MODULES.enable(); //Activate all auto_modules
		drivetrain.setSafetyEnabled(false);//WE DONT NEED SAFETY
	}

    @Override
    public void endAutonomous() { //when Autonomous is ended
    	drivetrain.setSafetyEnabled(true); //Maybe we do...
    	AUTO_MODULES.disable(); //Disabled auto_modules
    }
	
	@Override
	public void initTeleoperated() { //when Teleoperated in initalized
		TELEOP_MODULES.enable(); //Enable all teleop_modules
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
	public void periodicTeleoperated() { //runs periodically during Teleoperated
		controller1.doBinds();  //preforms every bind
		controller2.doBinds();
	}
	
	@Override
    public void endTeleoperated() { //when Teleoperated is disabled
        TELEOP_MODULES.disable(); //disable teleop_modules
    }

}

