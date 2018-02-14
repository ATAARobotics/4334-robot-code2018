package ca.fourthreethreefour.module.actuators;

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

	@Override
	public void setSpeed(double speed) {
		talon.set(speed);
	}

	@Override
	public void setRawSpeed(int speed) {
		talon.set((speed - 127) / 127);
	}

	@Override
	public double getSpeed() {
		return talon.get();
	}

	@Override
	public int getRawSpeed() {
		return (int) (talon.get() * 127 + 127);
	}

	@Override
	public void update() {
	}

	@Override
	public void setRate(double rate) {
		talon.set(rate);
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

	@Override
	public double getRate() {
		return talon.get();
	}

	@Override
	public double get() {
		return talon.get();
	}

	public void stopMotor() {
		talon.stopMotor();
	}
	
	@Override
	public void init() {
	}

	@Override
	protected void enableModule() {
	}

	@Override
	protected void disableModule() {
		this.set(0);
	}

}
