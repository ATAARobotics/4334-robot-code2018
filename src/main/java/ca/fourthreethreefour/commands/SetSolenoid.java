package main.java.ca.fourthreethreefour.commands;

import edu.first.command.Command;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;

/**
 * Sets the specified solenoid to specified direction
 * @author Cool Kornak
 *
 */

public final class SetSolenoid implements Command {
	
	private final DualActionSolenoid solenoid;
	private final Direction direction;
	
	public SetSolenoid (DualActionSolenoid solenoid, Direction direction) {
		this.solenoid = solenoid;
		this.direction = direction;
	}
	
	
	@Override
	public void run() {
		solenoid.set(direction);
		
	}

}
