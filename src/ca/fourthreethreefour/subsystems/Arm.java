package ca.fourthreethreefour.subsystems;

import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.subsystems.Subsystem;

public interface Arm {
	
	/*Creates DualActionSolenoidModules called armSolenoid, grabSolenoid, 
	and motorSolenoid, with ports specified. */
	DualActionSolenoidModule 
		grabSolenoid = new DualActionSolenoidModule(0, 1),
		motorSolenoid = new DualActionSolenoidModule(2, 3),
		armSolenoid = new DualActionSolenoidModule(4, 5);
	
	//Creates subsystem of above for use in Robot.java
	Subsystem arm = new Subsystem(new Module[] {  grabSolenoid, motorSolenoid, armSolenoid });

}
