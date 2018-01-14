package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.TalonSRXModule;
import ca.fourthreethreefour.module.TalonSRXModuleGroup;
import edu.first.identifiers.Function;
import edu.first.identifiers.InversedSpeedController;
import edu.first.module.Module;
import edu.first.module.actuators.Drivetrain;
import edu.first.module.subsystems.Subsystem;

public interface Drive extends Settings {
	TalonSRXModule 
		left1 = new TalonSRXModule(DRIVE_LEFT_1),
		left2 = new TalonSRXModule(DRIVE_LEFT_2),
		left3 = new TalonSRXModule(DRIVE_LEFT_3),
		right1 = new TalonSRXModule(DRIVE_RIGHT_1),
		right2 = new TalonSRXModule(DRIVE_RIGHT_2),
		right3 = new TalonSRXModule(DRIVE_RIGHT_3);
	
	TalonSRXModuleGroup 
		left = new TalonSRXModuleGroup(new TalonSRXModule[] { left1, left2, left3 }),
		right = new TalonSRXModuleGroup(new TalonSRXModule[] { right1, right2, right3 });

	//method drivetrain with inputs reversed left and right. Needs reversed so they move in same direction
	//TODO find out which side needs to be reversed
	Drivetrain drivetrain = new Drivetrain(new InversedSpeedController(left), right);
	
	/**
	 * Function that squares inputs.
	 */
	Function speedFunction = new Function() {
		@Override
		public double F(double in) {
			return in > 0 ? in * in : -(in * in);
		}
	};
	
	//TODO turnFunction goes here
	
	//creates subsystem called drive, with drivetrain, left, and right
	public Subsystem drive = new Subsystem(new Module[] { drivetrain, left, right });

}
