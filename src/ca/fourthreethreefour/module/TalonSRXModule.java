package ca.fourthreethreefour.module;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.first.module.actuators.SpeedController;
import edu.first.module.Module;

public class TalonSRXModule extends Module.StandardModule implements SpeedController {
	
	private final WPI_TalonSRX talon;
	
	protected TalonSRXModule(WPI_TalonSRX talon) {
		if(talon == null) {
            throw new NullPointerException("Null talon given");
		}
		this.talon = talon;
	}
	
	public TalonSRXModule(int channel) {
		this(new WPI_TalonSRX(channel));
	}

	/**
	 * @deprecated
	 */
	@Override
	public void setSpeed(double speed) {
		talon.set(speed);
	}

	/**
	 * @deprecated
	 */
	@Override
	public void setRawSpeed(int speed) {
	}

	@Override
	public double getSpeed() {
		return talon.get();
	}

	/**
	 * @deprecated
	 */
	@Override
	public int getRawSpeed() {
		return 0;
	}

	/**
	 * @deprecated
	 */
	@Override
	public void update() {
	}

	/**
	 * @deprecated
	 */
	@Override
	public void setRate(double rate) {
	}

	@Override
	public void set(double value) {
		talon.set(value);
	}
	
	public void setInverted(boolean isInverted) {
		talon.setInverted(isInverted);
	}
	
	public boolean getInverted(boolean isInverted) {
		return talon.getInverted();
	}

	/**
	 * @deprecated
	 */
	@Override
	public double getRate() {
		return 0;
	}

	@Override
	public double get() {
		return talon.get();
	}

	public void stopMotor() {
		talon.stopMotor();
	}
	
	/**
	 * @deprecated
	 */
	@Override
	public void init() {
	}

	/**
	 * @deprecated
	 */
	@Override
	protected void enableModule() {
	}

	@Override
	protected void disableModule() {
		talon.disable();
	}

}
