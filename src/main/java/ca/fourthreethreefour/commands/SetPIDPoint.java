package main.java.ca.fourthreethreefour.commands;

import edu.first.command.Command;
import main.java.ca.fourthreethreefour.subsystems.RotationalArm;

/**
 * Command designed to easily set a setpoint to RotationalArm or anything using
 * that class.
 * 
 * @author Cool Kornak
 *
 */

public final class SetPIDPoint implements Command {

	private final double setpoint;
	private final RotationalArm arm;
	
	public SetPIDPoint (RotationalArm arm, double setpoint) {
		this.arm = arm;
		this.setpoint = setpoint;
	}
	
	@Override
	public void run() {
		arm.set(setpoint);
	}

}
