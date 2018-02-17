package main.java.ca.fourthreethreefour.commands;

import main.java.ca.fourthreethreefour.settings.Settings;
import main.java.ca.fourthreethreefour.subsystems.Ramp.RampWinch;
import edu.first.command.Command;

public final class RampRetract implements Command, Settings {

	private final RampWinch ramp;
	
	// Constructor
	public RampRetract (RampWinch ramp) {
		this.ramp = ramp;
	}
	
	// Sets the specified RampWinch's speed
	@Override
	public void run() {
		ramp.set(RAMP_RETRACT_SPEED);
	}

}
