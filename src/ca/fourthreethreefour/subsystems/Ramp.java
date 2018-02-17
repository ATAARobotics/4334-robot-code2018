package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.module.actuators.TalonSRXModule;
import ca.fourthreethreefour.module.actuators.TalonSRXModuleGroup;
import ca.fourthreethreefour.settings.Settings;
import edu.first.identifiers.InversedSpeedController;
import edu.first.module.Module;
import edu.first.module.actuators.SolenoidModule;
import edu.first.module.actuators.SpeedController;
import edu.first.module.actuators.SpeedControllerGroup;
import edu.first.module.subsystems.Subsystem;

public interface Ramp extends Settings {
	
	/**
	 * Allows TalonSRXModule's to be controlled using SpeedControllers commands, but considered
	 * as a Module for Subsystem.
	 * @author Cool, with major credit to Joel.
	 *
	 */
	public static class RampWinch extends TalonSRXModuleGroup implements SpeedController {
		
		private final SpeedControllerGroup group;

		public RampWinch(TalonSRXModule controller1, TalonSRXModule controller2) { // Creates a public RampWinch with arguments being of two TalonSRXModule's
			super(new TalonSRXModule[] { controller1, controller2 }); // Calls the arguments/Constructs above
			this.group = new SpeedControllerGroup( // Initalizes with a SpeedControllerGroup with the arguments
					new SpeedController[] { new InversedSpeedController(controller1), controller2 });
		}

		@Override 
		public void set(double value) { // Allows the setting of a speed/value
			this.group.set(value);
		}

	}
	
	TalonSRXModule // Creates a TalonSRXModule for the left side and right
		leftRamp1 = new TalonSRXModule(RAMP_LEFT_1),
		leftRamp2 = new TalonSRXModule(RAMP_LEFT_2),
		rightRamp1 = new TalonSRXModule(RAMP_RIGHT_1),
		rightRamp2 = new TalonSRXModule(RAMP_RIGHT_2);
	
	RampWinch // Creates a module for the TalonSRXModule's designed for the RampWinch retraction.
		leftRamp = new RampWinch(leftRamp1, leftRamp2), //TODO Set correct TalonSRXModule to be reversed. First one is the reversed one
		rightRamp = new RampWinch(rightRamp1, rightRamp2);
	
	SolenoidModule // Creates a single action solenoid, with set ports.
		leftRelease = new SolenoidModule(RAMP_RELEASE_LEFT),
		rightRelease = new SolenoidModule(RAMP_RELEASE_RIGHT);
	
	// Makes a subsystem called ramp with parts above
	public Subsystem ramp = new Subsystem(new Module[] { leftRamp, rightRamp, leftRelease, rightRelease });
	
}
