package main.java.ca.fourthreethreefour.commands;

import edu.first.command.Command;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;

/**
 ** Sets the specified solenoid to LEFT
 ** @author Cool Kornak (with reference from ReverseSolenoid.java)
 **
 **/

public final class SolenoidLeft implements Command {
	
	private final DualActionSolenoid solenoid;
	
	public SolenoidLeft (DualActionSolenoid solenoid) {
		this.solenoid = solenoid;
	}
	
	
	@Override
	public void run() {
		if (solenoid.get() == Direction.RIGHT) {
		solenoid.set(DualActionSolenoid.Direction.LEFT);
		}
	}

}
