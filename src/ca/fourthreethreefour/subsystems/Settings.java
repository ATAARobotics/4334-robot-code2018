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
	int DRIVE_LEFT_1 = settingsFile.getIntProperty("DRIVE_LEFT_1", 1);
	int DRIVE_LEFT_2 = settingsFile.getIntProperty("DRIVE_LEFT_2", 2);
	int DRIVE_LEFT_3 = settingsFile.getIntProperty("DRIVE_LEFT_3", 3);
	int DRIVE_RIGHT_1 = settingsFile.getIntProperty("DRIVE_RIGHT_1", 4);
	int DRIVE_RIGHT_2 = settingsFile.getIntProperty("DRIVE_RIGHT_2", 5);
	int DRIVE_RIGHT_3 = settingsFile.getIntProperty("DRIVE_RIGHT_3", 6);
	int GRAB_SOLENOID_1 = settingsFile.getIntProperty("GRAB_SOLENOID_1", 0);
	int GRAB_SOLENOID_2 = settingsFile.getIntProperty("GRAB_SOLENOID_2", 1);
	int MOTOR_SOLENOID_1 = settingsFile.getIntProperty("MOTOR_SOLENOID_1", 2);
	int MOTOR_SOLENOID_2 = settingsFile.getIntProperty("MOTOR_SOLENOID_2", 3);
	int ARM_SOLENOID_1 = settingsFile.getIntProperty("ARM_SOLENOID_1", 4);
	int ARM_SOLENOID_2 = settingsFile.getIntProperty("ARM_SOLENOID_2", 5);

}
