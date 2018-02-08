package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.actuators.TalonSRXModule;
import ca.fourthreethreefour.settings.Settings;
import edu.first.identifiers.InversedSpeedController;
import edu.first.module.Module;
import edu.first.module.actuators.Drivetrain;
import edu.first.module.actuators.SolenoidModule;
import edu.first.module.subsystems.Subsystem;

public interface Ramp extends Settings {
	
	TalonSRXModule //Creates a TalonSRXModule for the left side and right
		leftRamp1 = new TalonSRXModule(RAMP_LEFT_1),
		leftRamp2 = new TalonSRXModule(RAMP_LEFT_2),
		rightRamp1 = new TalonSRXModule(RAMP_RIGHT_1),
		rightRamp2 = new TalonSRXModule(RAMP_RIGHT_2);
	
	Drivetrain
		leftRamp = new Drivetrain(new InversedSpeedController(leftRamp1), leftRamp2 ),
		rightRamp = new Drivetrain(new InversedSpeedController(rightRamp1), rightRamp2 );
	
	SolenoidModule
		leftRelease = new SolenoidModule(RAMP_RELEASE_LEFT),
		rightRelease = new SolenoidModule(RAMP_RELEASE_RIGHT);
	
	//VictorModule rampRelease = new VictorModule(0);
	
	//Makes a subsystem called ramp with parts above
	public Subsystem ramp = new Subsystem(new Module[] { leftRamp, rightRamp, leftRelease, rightRelease });
}
