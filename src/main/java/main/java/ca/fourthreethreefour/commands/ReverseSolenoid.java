package main.java.ca.fourthreethreefour.commands;

import edu.first.command.Command;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.Solenoid;

/**
 * Reverses the specified solenoid.
 * @author Aidan Paradis (copied from Trevor)
 *
 */
public final class ReverseSolenoid implements Command {

	private final DualActionSolenoid.Direction defaultDirection;
	private final DualActionSolenoid solenoid;

	public ReverseSolenoid (DualActionSolenoid solenoid) {
		this.solenoid = solenoid;
		this.defaultDirection = DualActionSolenoid.Direction.RIGHT;
	}

	public ReverseSolenoid(DualActionSolenoid solenoid, DualActionSolenoid.Direction defaultDirection) {
		this.solenoid = solenoid;
		this.defaultDirection = defaultDirection;
	}
	
	//Reverses the specified solenoid.
	@Override
	public void run() {
		if (solenoid.get() == DualActionSolenoid.Direction.OFF) {
			solenoid.set(defaultDirection);
		} else {
			solenoid.reverse();
		}
	}
}
