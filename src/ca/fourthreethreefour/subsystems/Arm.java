package ca.fourthreethreefour.subsystems;

import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.subsystems.Subsystem;

public interface Arm extends Settings {
	
	/*
	 * Creates DualActionSolenoidModules called armSolenoid, grabSolenoid, 
	 * and motorSolenoid, with ports specified. 
	 */
	DualActionSolenoidModule 
		grabSolenoid = new DualActionSolenoidModule(GRAB_SOLENOID_1, GRAB_SOLENOID_2),
		motorSolenoid = new DualActionSolenoidModule(MOTOR_SOLENOID_1, MOTOR_SOLENOID_2),
		armSolenoid = new DualActionSolenoidModule(ARM_SOLENOID_1, ARM_SOLENOID_2);
	
	//Creates subsystem of above for use in Robot.java
	Subsystem arm = new Subsystem(new Module[] {  grabSolenoid, motorSolenoid, armSolenoid });

}
