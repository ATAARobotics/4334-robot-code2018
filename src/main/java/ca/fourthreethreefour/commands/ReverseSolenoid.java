package main.java.ca.fourthreethreefour.commands;

import edu.first.command.Command;
import edu.first.module.actuators.DualActionSolenoid;

/**
 * Reverses the specified solenoid.
 * @author Aidan Paradis (copied from Trevor)
 *
 */
public final class ReverseSolenoid implements Command {
	
	private final DualActionSolenoid solenoid;

	public ReverseSolenoid (DualActionSolenoid solenoid) {
		this.solenoid = solenoid;
	}

	@Override
	public void run() {
		solenoid.reverse();
	}
}
