package ca.fourthreethreefour.subsystems;

import ca.fourthreethreefour.settings.Settings;
import edu.first.module.Module;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;

public interface Controllers extends Settings {
	
	XboxController
		controller1 = new XboxController(XBOXCONTROLLER_1),
		controller2 = new XboxController(XBOXCONTROLLER_2);
	
	Subsystem controllers = new Subsystem(new Module[] { controller1, controller2});
}
