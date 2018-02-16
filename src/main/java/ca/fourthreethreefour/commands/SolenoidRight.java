package main.java.ca.fourthreethreefour.commands;

import edu.first.command.Command;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;

/**
 ** Sets the specified solenoid to RIGHT
 ** @author Cool Kornak (with reference from ReverseSolenoid.java)
 **
 **/

public final class SolenoidRight implements Command {
	
	private final DualActionSolenoid solenoid;
	
	public SolenoidRight (DualActionSolenoid solenoid) {
		this.solenoid = solenoid;
	}
	
	
	@Override
	public void run() {
		if (solenoid.get() == Direction.LEFT) {
		solenoid.set(DualActionSolenoid.Direction.RIGHT);
		}
	}

}
