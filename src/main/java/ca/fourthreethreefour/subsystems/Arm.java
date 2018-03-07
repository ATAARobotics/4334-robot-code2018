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

	DualActionSolenoidModule 
		clawSolenoid = new DualActionSolenoidModule(CLAW_SOLENOID_1, CLAW_SOLENOID_2),
		flexSolenoid = new DualActionSolenoidModule(FLEX_SOLENOID_1, FLEX_SOLENOID_2);

    AnalogInput potentiometer = new AnalogInput(POTENTIOMETER);

	RotationalArm rotationalArm = new RotationalArm();

	Subsystem arm = new Subsystem(new Module[] {  clawSolenoid, flexSolenoid, rotationalArm, potentiometer });

	// aliases for directions, makes reading/setting directions easier
	DualActionSolenoid.Direction 
		CLAW_CLOSE = Direction.LEFT,
		CLAW_OPEN = Direction.RIGHT,
		FLEX_EXTEND = Direction.LEFT,
		FLEX_RETRACT = Direction.RIGHT;	

	// multiplies arm value by ARM_SPEED
	Function armFunction = new Function.ProductFunction(ARM_SPEED);
}
