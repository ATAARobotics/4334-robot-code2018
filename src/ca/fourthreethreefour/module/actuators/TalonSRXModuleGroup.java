package ca.fourthreethreefour.module.actuators;

import edu.first.module.actuators.SpeedController;
import edu.first.module.actuators.SpeedControllerGroup;
import edu.first.module.subsystems.Subsystem;

public class TalonSRXModuleGroup extends Subsystem implements SpeedController {

	private final SpeedControllerGroup group;
	
	public TalonSRXModuleGroup(TalonSRXModule[] group) {
		super(group);
		this.group = new SpeedControllerGroup(group);
	}

	/**
	 * @deprecated
	 */
	@Override
	public void setSpeed(double speed) {
		group.setSpeed(speed);
	}

	/**
	 * @deprecated
	 */
	@Override
	public void setRawSpeed(int speed) {
		group.setRawSpeed(speed);
	}

	/**
	 * @deprecated
	 */
	@Override
	public double getSpeed() {
		return group.getSpeed();
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
		group.set(value);
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
		return group.get();
	}
	
}
