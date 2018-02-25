package main.java.ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.first.identifiers.Output;
import edu.first.module.Module;
import edu.first.module.controllers.PIDController;
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
	
	public PIDController armPID = new PIDController(potentiometer, this, ARM_P, ARM_I, ARM_D); // Creates a PIDController for the arm
	
	public RotationalArm() { // Creates a public accessable class with a module of armMotor
		super(new Module[] { armMotor });
	}

	public boolean shouldArmBeFlexed() { // Checks if the arm's angle A.K.A the potentiometer value is between the set points.
		double armAngle = potentiometer.get();
		return (armAngle >= ARM_ANGLE_MIN && armAngle <= ARM_ANGLE_MAX && flexSolenoid.get() == FLEX_EXTEND);
	}

	@Override
	public void set(double value) {
		//Sets the setpoint for MotionMagic.
		armMotor.set(ControlMode.PercentOutput, value);

		// If it's true, meaning that the angle is between the min and max angles, it will set it to retract.
		if (shouldArmBeFlexed()) { flexSolenoid.set(FLEX_RETRACT); }
	}
		
}