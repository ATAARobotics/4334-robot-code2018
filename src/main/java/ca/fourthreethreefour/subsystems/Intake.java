package main.java.ca.fourthreethreefour.subsystems;

import edu.first.identifiers.Function;
import edu.first.identifiers.Output;
import edu.first.module.Module;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoidModule;
import edu.first.module.controllers.PIDController;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.first.module.sensors.AnalogInput;
import edu.first.module.subsystems.Subsystem;
import main.java.ca.fourthreethreefour.module.actuators.MotorModule;
import main.java.ca.fourthreethreefour.settings.Settings;

public interface Intake extends Settings {
	
	MotorModule // Creates a TalonSRXModule for the left side and right
		leftIntake = new MotorModule(TYPE_INTAKE_LEFT, INTAKE_LEFT),
		rightIntake = new MotorModule(TYPE_INTAKE_RIGHT, INTAKE_RIGHT),
		armIntake = new MotorModule(TYPE_INTAKE_ARM, INTAKE_ARM);
	
	DualActionSolenoidModule intakeSolenoid = new DualActionSolenoidModule(INTAKE_SOLENOID_1, INTAKE_SOLENOID_2);
	
    AnalogInput intakePotentiometer = new AnalogInput(INTAKE_POTENTIOMETER);

    public Output intakeOutput = new Output() {

		@Override
		public void set(double value) {
			armIntake.set(-value);
		}
    	
    };
    
    PIDController intakePID = new PIDController(intakePotentiometer, intakeOutput, INTAKE_P, INTAKE_I, INTAKE_D);
	    
	// Makes a subsystem called ramp with parts above
	public Subsystem intake = new Subsystem(new Module[] { leftIntake, rightIntake, intakeSolenoid, armIntake, intakePotentiometer, intakePID });
	
	DualActionSolenoid.Direction
		OPEN_INTAKE = Direction.LEFT,
		CLOSE_INTAKE = Direction.RIGHT;
	
	Function intakeArmFunction = new Function() {
		@Override
		public double F(double in) {
			return in > 0 ? in * in * INTAKE_ARM_SPEED_UP : -(in * in * INTAKE_ARM_SPEED_DOWN);
		}
};
	
}
