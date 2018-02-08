package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.actuators.TalonSRXModule;
import ca.fourthreethreefour.module.actuators.TalonSRXModuleGroup;
import ca.fourthreethreefour.settings.Settings;
import edu.first.identifiers.Function;
import edu.first.identifiers.InversedSpeedController;
import edu.first.module.Module;
import edu.first.module.actuators.Drivetrain;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.subsystems.Subsystem;

public interface Drive extends Settings {
	
	TalonSRXModule // Creates modules on these ports. Ports are determined in settings.txt on the RoboRIO,
		left1 = new TalonSRXModule(DRIVE_LEFT_1), // or the default ports in Settings.java.
		left2 = new TalonSRXModule(DRIVE_LEFT_2),
		right1 = new TalonSRXModule(DRIVE_RIGHT_1),
		right2 = new TalonSRXModule(DRIVE_RIGHT_2);
	
	TalonSRXModuleGroup // Groups Modules together so they can be used as one speed controller.
		left = new TalonSRXModuleGroup(new TalonSRXModule[] { left1, left2 }),
		right = new TalonSRXModuleGroup(new TalonSRXModule[] { right1, right2 });

	// Drivetrain object using the TalonSRX groups left and right. Left is reversed so they move in the same direction.
	// TODO find out which side needs to be reversed
	Drivetrain drivetrain = new Drivetrain(new InversedSpeedController(left), right);
	
	
	DualActionSolenoidModule // DualActionSolenoid called gearShifter
		gearShifter = new DualActionSolenoidModule(GEAR_SHIFTER_SOLENOID_1, GEAR_SHIFTER_SOLENOID_2);
	
	DualActionSolenoid.Direction LOW_GEAR = Direction.LEFT;
	DualActionSolenoid.Direction HIGH_GEAR = Direction.RIGHT;
	
	/**
	 * Function used in driving controls that squares the input of the joysticks on the controller. 
	 * This is done to make controls more intuitive.
	 */
	Function  
		speedFunction = new Function() { // makes a function for speed
			@Override
			public double F(double in) { // sets the function to return a double
				return in > 0 ? in * in : -(in * in); //if in is greater than 0, multiply it by itself
				//^ otherwise multiply by itself and make it negative
			}
		};
		// TODO decide if we need a function for turning, and add it if we do
	
	// Creates subsystem called drive, with Modules drivetrain, left, right, and gearShifter
	public Subsystem drive = new Subsystem(new Module[] { drivetrain, left, right, gearShifter });

}
