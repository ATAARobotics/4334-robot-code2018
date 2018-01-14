package ca.fourthreethreefour.subsystems;

import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.subsystems.Subsystem;

public interface Arm {
	
	/*Creates DualActionSolenoidModules called wristSolenoid, grabSolenoid, 
	and motorSolenoid, with ports specified. */
	DualActionSolenoidModule 
		wristSolenoid = new DualActionSolenoidModule(0, 1),
		grabSolenoid = new DualActionSolenoidModule(2, 3),
		motorSolenoid = new DualActionSolenoidModule(4, 5),
		armSolenoid = new DualActionSolenoidModule(6, 7);
	
	//Creates subsystem of above for use in Robot.java
	Subsystem arm = new Subsystem(new Module[] { wristSolenoid, grabSolenoid, motorSolenoid, armSolenoid });

}
