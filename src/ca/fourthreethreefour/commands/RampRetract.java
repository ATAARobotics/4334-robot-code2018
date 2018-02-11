package ca.fourthreethreefour.commands;

import ca.fourthreethreefour.settings.Settings;
import ca.fourthreethreefour.subsystems.Ramp.RampWinch;
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
