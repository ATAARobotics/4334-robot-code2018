package main.java.ca.fourthreethreefour.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.first.identifiers.Output;
import edu.first.module.Module;
import edu.first.module.sensors.DigitalInput;
import edu.first.module.subsystems.Subsystem;
import main.java.ca.fourthreethreefour.settings.Settings;


/**
 * Puts the full rotation parts of the arm into one class.
 * Designed to prevent going past the limit.
 * 
 * @author Cool and Joel
 */
public class RotationalArm extends Subsystem implements Settings, Output, Arm {
	public static VictorSPX armMotor = new VictorSPX(ARM_MOTOR);
	public static DigitalInput 
		highLimitSwitch = new DigitalInput(HIGH_LIMIT_SWITCH),
		lowLimitSwitch = new DigitalInput(LOW_LIMIT_SWITCH);
	
	public RotationalArm() { // Creates a public accessable class with a module of armMotor, highLimitSwitch, and lowLimitSwitch
		super(new Module[] { highLimitSwitch, lowLimitSwitch });
	}

	@Override
	public void set(double value) {
		armMotor.set(ControlMode.PercentOutput, value);
	}
}