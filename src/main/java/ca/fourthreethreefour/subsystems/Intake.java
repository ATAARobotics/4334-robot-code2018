package main.java.ca.fourthreethreefour.subsystems;

import edu.first.module.Module;
import edu.first.module.actuators.SolenoidModule;
import edu.first.module.subsystems.Subsystem;
import main.java.ca.fourthreethreefour.module.actuators.MotorModule;
import main.java.ca.fourthreethreefour.settings.Settings;

public interface Intake extends Settings {
	
	/**
	 * Allows TalonSRXModule's to be controlled using SpeedControllers commands, but considered
	 * as a Module for Subsystem.
	 * @author Cool, with major credit to Joel.
	 *
	 */
	
	MotorModule // Creates a TalonSRXModule for the left side and right
		leftRamp1 = new MotorModule(TYPE_RAMP_LEFT_1, RAMP_LEFT_1),
		rightRamp1 = new MotorModule(TYPE_RAMP_RIGHT_1, RAMP_RIGHT_1);
	
	/*SolenoidModule // Creates a single action solenoid, with set ports.
		leftRelease = new SolenoidModule(RAMP_RELEASE_LEFT),
		rightRelease = new SolenoidModule(RAMP_RELEASE_RIGHT);
		*/
	
	// Makes a subsystem called ramp with parts above
	public Subsystem intake = new Subsystem(new Module[] {
			leftRamp1, rightRamp1,
	});
	
}
