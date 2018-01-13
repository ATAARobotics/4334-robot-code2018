package ca.fourthreethreefour.subsystems;

import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.subsystems.Subsystem;

public interface Arm {
	
	//Creates a DualACtionSolenoidModule called wristSolenoid, with ports x,y
	DualActionSolenoidModule wristSolenoid = new DualActionSolenoidModule(0, 1);
	
	//Creates a DualActionSolenoidModule called grabSolenoid, with ports x,y
	DualActionSolenoidModule grabSolenoid = new DualActionSolenoidModule(2, 3);
	
	//Creates a DualActionSolenoidModule called motorSolenoid, with ports x,y
	DualActionSolenoidModule motorSolenoid = new DualActionSolenoidModule(4, 5);
	
	//Creates subsystem of above for use in Robot.java
	Subsystem arm = new Subsystem(new Module[] { wristSolenoid, grabSolenoid, motorSolenoid });

}
