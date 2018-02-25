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
 * Puts the parts of the arm that rotate and parts needed in one class that can be called upon with specific functions.
 * 
 * @author Cool with assistance from Joel
 */
public class RotationalArm extends Subsystem implements Settings, Output, Arm {
	
	public static VictorSPXModule armMotor = new VictorSPXModule(ARM_MOTOR); // Creates a *Module called armMotor that can be called on
	
	Input potentiometer = new AnalogInput(POTENTIOMETER); // Creates input that recieves from AnalogInput
	
	public PIDController armPID = new PIDController(potentiometer, this, ARM_P, ARM_I, ARM_D); // Creates a PIDController for the arm
	
	public RotationalArm() { // Creates a public accessable class with a module of armMotor
		super(new Module[] { armMotor });
	}

	public boolean shouldArmBeFlexed() { // Checks if the arm's angle A.K.A the potentiometer value is between the set points.
		double armAngle = potentiometer.get();
		return (armAngle >= ARM_ANGLE_MIN && armAngle <= ARM_ANGLE_MAX);
	}

	@Override
	public void set(double value) { // Sets the armMotor and checks if Arm should be flexed. Value is set from this in PIDController.
		
		armMotor.set(value);
		
		if (rotationalArm.shouldArmBeFlexed()) { flexSolenoid.set(FLEX_RETRACT); } // If it's true, meaning that the angle is between the min and max angles, it will set it to retract.

	}
		
}