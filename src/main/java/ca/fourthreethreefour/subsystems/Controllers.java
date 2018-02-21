package main.java.ca.fourthreethreefour.subsystems;

import main.java.ca.fourthreethreefour.settings.Settings;
import edu.first.module.Module;
import edu.first.module.joysticks.XboxController;
import edu.first.module.subsystems.Subsystem;

public interface Controllers extends Settings {
	
	XboxController
		controller1 = new XboxController(XBOXCONTROLLER_1),
		controller2 = new XboxController(XBOXCONTROLLER_2);
	edu.wpi.first.wpilibj.XboxController controllerPOV = new edu.wpi.first.wpilibj.XboxController(XBOXCONTROLLER_2);
	
	Subsystem controllers = new Subsystem(new Module[] { controller1, controller2});
}
