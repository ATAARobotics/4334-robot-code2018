package ca.fourthreethreefour.subsystems;

import edu.first.identifiers.Function;
import edu.first.identifiers.InversedSpeedController;
import edu.first.module.Module;
import edu.first.module.actuators.Drivetrain;
import edu.first.module.subsystems.Subsystem;

public interface Drive extends Settings {
	
	//TODO CANTalonSRX for left wheels. Call it 'left'
	
	//TODO CANTalonSRX for right wheels. Call it 'right'
	
	//method drivetrain with inputs reversed left and right. Needs reversed so they move in same direction
	Drivetrain drivetrain = new Drivetrain(new InversedSpeedController(left), right);
	
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
