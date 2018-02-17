package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.actuators.TalonSRXModule;
import ca.fourthreethreefour.settings.Settings;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.subsystems.Subsystem;

public interface Arm extends Settings {
	
	/*
	 * Creates DualActionSolenoidModules called armSolenoid, grabSolenoid, 
	 * and motorSolenoid, with ports specified. 
	 */
	DualActionSolenoidModule 
		clawSolenoid = new DualActionSolenoidModule(CLAW_SOLENOID_1, CLAW_SOLENOID_2),
		flexSolenoid = new DualActionSolenoidModule(FLEX_SOLENOID_1, FLEX_SOLENOID_2);
	
	TalonSRXModule armMotor = new TalonSRXModule(ARM_MOTOR);
	
	// Creates subsystem of above for use in Robot.java
	Subsystem arm = new Subsystem(new Module[] {  clawSolenoid, flexSolenoid, armMotor });

	/*
	 *  Creates two direction (functions?) that can be used in Autonomous for easy setting of their solenoid.
	 *  TODO Update these with correct direction.
	 */
	DualActionSolenoid.Direction CLAW_CLOSE = Direction.LEFT;
	DualActionSolenoid.Direction CLAW_OPEN = Direction.RIGHT;
	// grabSolenoid ^
	
	DualActionSolenoid.Direction FLEX_EXTEND = Direction.LEFT;
	DualActionSolenoid.Direction FLEX_RETRACT = Direction.RIGHT;	
	// armSolenoid ^
}
