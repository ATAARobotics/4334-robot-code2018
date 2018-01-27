package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.TalonSRXModule;
import ca.fourthreethreefour.settings.Settings;
import edu.first.identifiers.InversedSpeedController;
import edu.first.module.Module;
import edu.first.module.actuators.Drivetrain;
import edu.first.module.subsystems.Subsystem;

public interface Ramp extends Settings {

	TalonSRXModule //Creates a TalonSRXModule for the left side and right
		leftRamp = new TalonSRXModule(RAMP_LEFT),	//not sure if having it as just left and right would be a conflict with drive, just incase
		rightRamp = new TalonSRXModule(RAMP_RIGHT);
	
	//VictorModule rampRelease = new VictorModule(0);
	
	//Perhaps not the best method for this, but links both motors together in a drivetrain called ramptrain
	Drivetrain rampTrain = new Drivetrain(leftRamp, new InversedSpeedController(rightRamp));
	
	//Makes a subsystem called ramp with parts above
	public Subsystem ramp = new Subsystem(new Module[] { leftRamp, rightRamp, rampTrain });
}
