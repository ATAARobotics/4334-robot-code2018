package main.java.ca.fourthreethreefour.subsystems;

import edu.first.identifiers.Input;
import edu.first.identifiers.Output;
import edu.first.module.Module;
import edu.first.module.controllers.PIDController;
import edu.first.module.sensors.AnalogInput;
import edu.first.module.subsystems.Subsystem;
import main.java.ca.fourthreethreefour.module.actuators.VictorSPXModule;
import main.java.ca.fourthreethreefour.settings.Settings;


/**
 * Puts the full rotation parts of the arm into one class.
 * Designed to prevent going past the limit.
 * 
 * @author Cool and Joel
 */
public class RotationalArm extends Subsystem implements Settings, Output, Arm {
	
	public static VictorSPXModule armMotor = new VictorSPXModule(ARM_MOTOR);
	
	Input potentiometer = new AnalogInput(POTENTIOMETER);
	
	public PIDController armPID = new PIDController(potentiometer, this, ARM_P, ARM_I, ARM_D);
	
	public RotationalArm() { // Creates a public accessable class with a module of armMotor, highLimitSwitch, and lowLimitSwitch
		super(new Module[] { armMotor });
	}

	public boolean shouldArmBeFlexed() {
		double armAngle = potentiometer.get();
		return (armAngle >= ARM_ANGLE_MIN && armAngle <= ARM_ANGLE_MAX);
	}

	@Override
	public void set(double value) {
		
		armMotor.set(value);
		
		if (rotationalArm.shouldArmBeFlexed()) { flexSolenoid.set(FLEX_RETRACT); } // If it's true, meaning that the angle is between the min and max angles, it will set it to retract.

	}
		
}