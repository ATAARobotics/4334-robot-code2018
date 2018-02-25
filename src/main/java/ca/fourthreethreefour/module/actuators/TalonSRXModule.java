package main.java.ca.fourthreethreefour.module.actuators;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.first.module.actuators.SpeedController;
import edu.first.module.Module;

/**
 * The general purpose class that manipulates Talon speed controllers made by
 * IFI / CTRE. Should work for all models <b> through CAN</b>.
 *
 * @since Feb 13 18
 * @author Trevor Tsang
 */
public class TalonSRXModule extends Module.StandardModule implements SpeedController  {
	
	private final WPI_TalonSRX talon;
	
	protected TalonSRXModule(WPI_TalonSRX talon) {
		if(talon == null) {
            throw new NullPointerException("Null talon given");
		}
		this.talon = talon;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public TalonSRXModule(int channel) {
		this(new WPI_TalonSRX(channel));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void enableModule() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void disableModule() {
		this.set(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSpeed(double speed) {
		talon.set(speed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRawSpeed(int speed) {
		talon.set((speed - 127) / 127);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getSpeed() {
		return talon.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRawSpeed() {
		return (int) (talon.get() * 127 + 127);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRate(double rate) {
		talon.set(rate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(double value) {
		talon.set(value);
	}
	
	/**
	 * Sets the appropriate output on the talon, depending on the mode.
	 * @param mode The output mode to apply.
	 * In PercentOutput, the output is between -1.0 and 1.0, with 0.0 as stopped.
	 * In Current mode, output value is in amperes.
	 * In Velocity mode, output value is in position change / 100ms.
	 * In Position mode, output value is in encoder ticks or an analog value,
	 *   depending on the sensor. 
	 * In Follower mode, the output value is the integer device ID of the talon to
	 * duplicate.
	 *
	 * @param outputValue The setpoint value, as described above.
	 */
	public void set(ControlMode mode, double outputValue) {
		talon.set(mode, outputValue);
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void setInverted(boolean isInverted) {
		talon.setInverted(isInverted);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getInverted(boolean isInverted) {
		return talon.getInverted();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getRate() {
		return talon.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double get() {
		return talon.get();
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopMotor() {
		talon.stopMotor();
	}

	/**
	 * {@inheritDoc}
	 * @return object that can get/set individual raw sensor values.
	 */
	public Object getSensorCollection() {
		return talon.getSensorCollection();
	}

	/**
	 * {@inheritDoc}
	 * @return
	 */
	public double getAnalogIn() {
		return talon.getSensorCollection().getAnalogIn();
	}
	
}
