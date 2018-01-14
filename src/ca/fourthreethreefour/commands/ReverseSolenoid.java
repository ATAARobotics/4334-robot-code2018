package ca.fourthreethreefour.commands;

import edu.first.command.Command;
import edu.first.module.actuators.DualActionSolenoid;

/**
 * Reverses the specified solenoid.
 * @author Aidan Paradis (copied from Trevor)
 *
 */
public final class ReverseSolenoid implements Command {
	
	private final DualActionSolenoid solenoid;
	
	//Constructor for the command
	public ReverseSolenoid (DualActionSolenoid solenoid) {
		this.solenoid = solenoid;
	}
	
	//Reverses the specified solenoid.
	@Override
	public void run() {
		solenoid.reverse();
	}
}
