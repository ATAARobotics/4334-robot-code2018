package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.actuators.TalonSRXModule;
import ca.fourthreethreefour.settings.Settings;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.sensors.EncoderModule;
import edu.first.module.subsystems.Subsystem;

public interface Arm extends Settings {
	
	/*
	 * Creates DualActionSolenoidModules called armSolenoid, grabSolenoid, 
	 * and motorSolenoid, with ports specified. 
	 */
	DualActionSolenoidModule 
		grabSolenoid = new DualActionSolenoidModule(GRAB_SOLENOID_1, GRAB_SOLENOID_2),
		armSolenoid = new DualActionSolenoidModule(ARM_SOLENOID_1, ARM_SOLENOID_2);
	
	TalonSRXModule armMotor = new TalonSRXModule(ARM_MOTOR);
	
	
	// Creates subsystem of above for use in Robot.java
	Subsystem arm = new Subsystem(new Module[] {  grabSolenoid, armSolenoid, armMotor });

	/*
	 *  Creates two direction (functions?) that can be used in Autonomous for easy setting of their solenoid.
	 *  TODO Update these with correct direction.
	 */
	DualActionSolenoid.Direction GRAB_CLOSE = Direction.LEFT;
	DualActionSolenoid.Direction GRAB_OPEN = Direction.RIGHT;
	// grabSolenoid ^
	
	DualActionSolenoid.Direction ARM_EXTEND = Direction.LEFT;
	DualActionSolenoid.Direction ARM_RETRACT = Direction.RIGHT;	
	// armSolenoid ^
}
