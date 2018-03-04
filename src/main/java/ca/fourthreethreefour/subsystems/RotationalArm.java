package main.java.ca.fourthreethreefour.subsystems;

import edu.first.identifiers.Output;
import edu.first.module.Module;
import edu.first.module.controllers.PIDController;
import edu.first.module.subsystems.Subsystem;
import main.java.ca.fourthreethreefour.module.actuators.MotorModule;
import main.java.ca.fourthreethreefour.module.actuators.MotorModule.Type;
import main.java.ca.fourthreethreefour.settings.Settings;


/**
 * Puts the parts of the arm that rotate and parts needed in one class that can be called upon with specific functions.
 * 
 * @author Cool with assistance from Joel
 */
public class RotationalArm extends Subsystem implements Settings, Arm {
	
	public static MotorModule armMotor = new MotorModule(Type.VICTOR_SPX, ARM_MOTOR);

	public static boolean shouldArmBeFlexed() { // Checks if the arm's angle A.K.A the potentiometer value is between the set points.
		double armAngle = potentiometer.get();
		return (armAngle >= ARM_ANGLE_MIN && armAngle <= ARM_ANGLE_MAX && flexSolenoid.get() == FLEX_EXTEND);
	}
	
	public static PIDController armPID = new PIDController(potentiometer, new Output() {
		@Override
		public void set(double value) {
			// Sets the setpoint for MotionMagic.
			armMotor.set(-value);

			// If it's true, meaning that the angle is between the min and max angles, it will set it to retract.
			if (shouldArmBeFlexed()) { flexSolenoid.set(FLEX_RETRACT); }
		}
	}, ARM_P, ARM_I, ARM_D);
	
	public RotationalArm() {
		super(new Module[] { armMotor });
	}
}