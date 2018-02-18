package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.actuators.TalonSRXModule;
import ca.fourthreethreefour.settings.Settings;
import edu.first.identifiers.Output;
import edu.first.module.Module;
import edu.first.module.sensors.DigitalInput;
import edu.first.module.subsystems.Subsystem;

public class RotationalArm extends Subsystem implements Settings, Output {
	public static TalonSRXModule armMotor = new TalonSRXModule(ARM_MOTOR);
	public static DigitalInput highLimitSwitch = new DigitalInput(HIGH_LIMIT_SWITCH);
	public static DigitalInput lowLimitSwitch = new DigitalInput(LOW_LIMIT_SWITCH);
	
	public RotationalArm() {
		super(new Module[] { armMotor, highLimitSwitch, lowLimitSwitch });
	}

	@Override
	public void set(double value) {
		// TODO Figure out if true or false means limit reached, if false add a ! before
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

	public double getAnalog() {
		return armMotor.getAnalogIn();
	}
}