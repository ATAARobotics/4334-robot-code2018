package main.java.ca.fourthreethreefour.subsystems;

import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.subsystems.Subsystem;
import main.java.ca.fourthreethreefour.module.actuators.MotorModule;
import main.java.ca.fourthreethreefour.settings.Settings;

public interface Intake extends Settings {
	
	MotorModule // Creates a TalonSRXModule for the left side and right
		leftIntake = new MotorModule(TYPE_INTAKE_LEFT, INTAKE_LEFT),
		rightIntake = new MotorModule(TYPE_INTAKE_RIGHT, INTAKE_RIGHT),
		armIntake = new MotorModule(TYPE_INTAKE_ARM, INTAKE_ARM);
	
	DualActionSolenoidModule intakeSolenoid = new DualActionSolenoidModule(INTAKE_SOLENOID_1, INTAKE_SOLENOID_2);
	
	// Makes a subsystem called ramp with parts above
	public Subsystem intake = new Subsystem(new Module[] { leftIntake, rightIntake, intakeSolenoid });
	
	DualActionSolenoid.Direction
		OPEN_INTAKE = Direction.LEFT,
		CLOSE_INTAKE = Direction.RIGHT;
	
	
}
