package ca.fourthreethreefour.subsystems;

import java.io.File;

/**
 * Contains all values {@link SettingsFile} can parse, as well as their keys and default values.
 * @author Trevor, but also Joel
 *
 */
public interface Settings {

	SettingsFile settingsFile = new SettingsFile(new File("/settings.txt"));
	
	//TODO ports
	//int EXAMPLE_PORT = settingsFile.getIntProperty("EXAMPLE_PORT", [default port])
	int GRAB_SOLENOID_1 = settingsFile.getIntProperty("GRAB_SOLENOID_1", 0);
	int GRAB_SOLENOID_2 = settingsFile.getIntProperty("GRAB_SOLENOID_2", 1);
	int MOTOR_SOLENOID_1 = settingsFile.getIntProperty("MOTOR_SOLENOID_1", 2);
	int MOTOR_SOLENOID_2 = settingsFile.getIntProperty("MOTOR_SOLENOID_2", 3);
	int ARM_SOLENOID_1 = settingsFile.getIntProperty("ARM_SOLENOID_1", 4);
	int ARM_SOLENOID_2 = settingsFile.getIntProperty("ARM_SOLENOID_2", 5);

}
