package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.actuators.TalonSRXModule;
import ca.fourthreethreefour.settings.Settings;
import edu.first.identifiers.Output;
import edu.first.module.Module;
import edu.first.module.sensors.DigitalInput;
import edu.first.module.subsystems.Subsystem;


/**
 * Puts the full rotation parts of the arm into one class.
 * Designed to prevent going past the limit.
 * 
 * @author Cool and Joel
 */
public class RotationalArm extends Subsystem implements Settings, Output, Arm {
	public static TalonSRXModule armMotor = new TalonSRXModule(ARM_MOTOR);
	public static DigitalInput 
		highLimitSwitch = new DigitalInput(HIGH_LIMIT_SWITCH),
		lowLimitSwitch = new DigitalInput(LOW_LIMIT_SWITCH);
	
	public RotationalArm() { // Creates a public accessable class with a module of armMotor, highLimitSwitch, and lowLimitSwitch
		super(new Module[] { armMotor, highLimitSwitch, lowLimitSwitch });
	}

	@Override
	public void set(double value) {
		// TODO Figure out if true or false means limit reached, if false add a ! before
		/*
		 * Gets the current position of the respective limit switch, then if the value
		 * is above or below 0 for each respective, then it will set the value, otherwise it won't.
		 */
		if (highLimitSwitch.getPosition()) {
			if (value < 0) {
				armMotor.set(value);
			}
		}
		if (lowLimitSwitch.getPosition()) {
			if (value > 0) {
				armMotor.set(value);
			}
		}
	}

	
	public boolean setArmFlex() {
		double armAngle = armMotor.getAnalogIn();
		if (armAngle >= ARM_ANGLE_MIN && armAngle <= ARM_ANGLE_MAX) {
			return true;
		} else { return false; }
	}
}