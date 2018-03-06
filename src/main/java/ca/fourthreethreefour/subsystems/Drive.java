package main.java.ca.fourthreethreefour.subsystems;

import edu.first.identifiers.Function;
import edu.first.identifiers.InversedSpeedController;
import edu.first.module.Module;
import edu.first.module.actuators.Drivetrain;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.actuators.SpeedController;
import edu.first.module.actuators.SpeedControllerGroup;
import edu.first.module.subsystems.Subsystem;
import main.java.ca.fourthreethreefour.module.actuators.MotorModule;
import main.java.ca.fourthreethreefour.module.actuators.TalonSRXModule;
import main.java.ca.fourthreethreefour.module.actuators.TalonSRXModuleGroup;
import main.java.ca.fourthreethreefour.settings.Settings;

public interface Drive extends Settings {
	
	MotorModule // Creates modules on these ports. Ports are determined in settings.txt on the RoboRIO,
		left1 = new MotorModule(TYPE_DRIVE_LEFT_1, DRIVE_LEFT_1), // or the default ports in Settings.java.
		left2 = new MotorModule(TYPE_DRIVE_LEFT_2, DRIVE_LEFT_2),
		right1 = new MotorModule(TYPE_DRIVE_RIGHT_1, DRIVE_RIGHT_1),
		right2 = new MotorModule(TYPE_DRIVE_RIGHT_2, DRIVE_RIGHT_2);
	
	SpeedControllerGroup // Groups Modules together so they can be used as one speed controller.
		left = new SpeedControllerGroup(new SpeedController[] { left1, left2 }),
		right = new SpeedControllerGroup(new SpeedController[] { right1, right2 });

	// Drivetrain object using the TalonSRX groups left and right. One side is reversed so they move in the same direction.
	Drivetrain 
		drivetrain = new Drivetrain(new InversedSpeedController(left), right);
	
	DualActionSolenoidModule // DualActionSolenoid called gearShifter
		gearShifter = new DualActionSolenoidModule(GEAR_SHIFTER_SOLENOID_1, GEAR_SHIFTER_SOLENOID_2);
	
	DualActionSolenoid.Direction
		LOW_GEAR = Direction.LEFT,
		HIGH_GEAR = Direction.RIGHT;
	
	/**
	 * Function used in driving controls that squares the input of the joysticks on the controller. 
	 * This is done to make controls more intuitive.
	 */
	Function speedFunction = new Function() {
			@Override
			public double F(double in) {
				// if in is greater than 0, multiply it by itself, otherwise multiply by itself and make it negative
				return in > 0 ? in * in : -(in * in);
			}
	};

	Function turnFunction = new Function() {
		@Override
		public double F(double in) {
			//if in is greater than 0, bring it to the power of TURN_CURVE
			//otherwise bring its absolute value to the power of TURN_CURVE and make it negative
			return in > 0 ? Math.pow(in, TURN_CURVE) : -Math.pow(Math.abs(in), TURN_CURVE);
		}
	};

	// Creates subsystem called drive, with Modules drivetrain, left1, left2, right1, right2, and gearShifter
	public Subsystem drive = new Subsystem(new Module[] { drivetrain, left1, left2, right1, right2, gearShifter, });

}
