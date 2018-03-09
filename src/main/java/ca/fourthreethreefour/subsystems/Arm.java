package main.java.ca.fourthreethreefour.subsystems;

import edu.first.identifiers.Function;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.sensors.AnalogInput;
import edu.first.module.subsystems.Subsystem;
import main.java.ca.fourthreethreefour.settings.Settings;

public interface Arm extends Settings {
	
	/*
	 * Creates DualActionSolenoidModules called clawSolenoid and flexSolenoid, with ports specified. 
	 */
	DualActionSolenoidModule 
		clawSolenoid = new DualActionSolenoidModule(CLAW_SOLENOID_1, CLAW_SOLENOID_2),
		flexSolenoid = new DualActionSolenoidModule(FLEX_SOLENOID_1, FLEX_SOLENOID_2);

    AnalogInput potentiometer = new AnalogInput(POTENTIOMETER);

	RotationalArm rotationalArm = new RotationalArm();

	// Creates subsystem of above for use in Robot.java
	Subsystem arm = new Subsystem(new Module[] {  clawSolenoid, flexSolenoid, rotationalArm, potentiometer });

	/*
	 *  Creates two directions that can be used in Autonomous for easy setting of their respective solenoid.
	 *  TODO Update these with correct direction.
	 */
	DualActionSolenoid.Direction 
		CLAW_CLOSE = Direction.LEFT,
		CLAW_OPEN = Direction.RIGHT,
		FLEX_EXTEND = Direction.LEFT,
		FLEX_RETRACT = Direction.RIGHT;	
	
	// Squares the input and multiplies the result by ARM_SPEED. 
	// If the input is negative, it makes the result negative.
	Function armFunction = new Function() {
		public double F(double in) {
			return in > 0 ? in * in * ARM_SPEED : -(in * in * ARM_SPEED);
		}
	};
}
