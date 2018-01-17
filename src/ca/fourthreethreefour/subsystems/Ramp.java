package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.TalonSRXModule;
import edu.first.module.Module;
import edu.first.module.subsystems.Subsystem;

public interface Ramp extends Settings {

	TalonSRXModule //Creates a TalonSRXModule for the left side and right
		left = new TalonSRXModule(RAMP_LEFT),
		right = new TalonSRXModule(RAMP_RIGHT);
	
	//Makes a subsystem called ramp with parts above
	public Subsystem ramp = new Subsystem(new Module[] { left, right });
}
