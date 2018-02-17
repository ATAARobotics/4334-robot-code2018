package ca.fourthreethreefour.commands;

import edu.first.command.Command;
import edu.first.module.actuators.DualActionSolenoid;
import edu.first.module.actuators.DualActionSolenoid.Direction;

/**
 ** Sets the specified solenoid to LEFT
 ** @author Cool Kornak (with reference from ReverseSolenoid.java)
 **
 **/

public final class SolenoidDirection implements Command {
	
	private final DualActionSolenoid solenoid;
	private final String side;
	
	public SolenoidDirection (DualActionSolenoid solenoid, String side) {
		this.solenoid = solenoid;
		this.side = side.toUpperCase();
	}
	
	
	@Override
	public void run() {
		switch (side) {
		case "LEFT":
			if (solenoid.get() == Direction.RIGHT) {
				solenoid.set(DualActionSolenoid.Direction.LEFT);
				}
		case "RIGHT":
			if (solenoid.get() == Direction.LEFT) {
				solenoid.set(DualActionSolenoid.Direction.RIGHT);
				}
		case "":
			solenoid.set(DualActionSolenoid.Direction.OFF);
			throw new Error("Error in SolenoidDirection: No Direction set");
		default:
			throw new Error("Error in SolenoidDirection: Direction set incorrectly");
		}
		
	}

}
