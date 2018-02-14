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

	@Override
	public void setSpeed(double speed) {
		group.setSpeed(speed);
	}

	@Override
	public void setRawSpeed(int speed) {
		group.setRawSpeed(speed);
	}

	@Override
	public double getSpeed() {
		return group.getSpeed();
	}

	@Override
	public int getRawSpeed() {
		return group.getRawSpeed();
	}

	@Override
	public void update() {
	}

	@Override
	public void setRate(double rate) {
		group.set(rate);
	}

	@Override
	public void set(double value) {
		group.set(value);
	}

	@Override
	public double getRate() {
		return group.get();
	}

	@Override
	public double get() {
		return group.get();
	}
	
}
