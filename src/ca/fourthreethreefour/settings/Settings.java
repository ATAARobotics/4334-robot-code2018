package ca.fourthreethreefour.settings;

import java.io.File;

/**
 * Contains all values {@link SettingsFile} can parse, as well as their keys and default values.
 * @author Trevor, but also Joel
 * 
 */
public interface Settings {

	SettingsFile settingsFile = new SettingsFile(new File("/settings.txt"));

    String AUTO_TYPE = settingsFile.getProperty("AUTO_TYPE", "");
	
    boolean LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);
	
    // Ports
	// TODO get default ports
	// int EXAMPLE_PORT = settingsFile.getIntProperty("EXAMPLE_PORT", [default port])
	int DRIVE_LEFT_1 = settingsFile.getIntProperty("DRIVE_LEFT_1", 1);
	int DRIVE_LEFT_2 = settingsFile.getIntProperty("DRIVE_LEFT_2", 2);
	int DRIVE_LEFT_3 = settingsFile.getIntProperty("DRIVE_LEFT_3", 3);
	int DRIVE_RIGHT_1 = settingsFile.getIntProperty("DRIVE_RIGHT_1", 4);
	int DRIVE_RIGHT_2 = settingsFile.getIntProperty("DRIVE_RIGHT_2", 5);
	int DRIVE_RIGHT_3 = settingsFile.getIntProperty("DRIVE_RIGHT_3", 6);
	//TODO Update to 4 ports (2 ports each)
	
	int ARM_MOTOR = settingsFile.getIntProperty("ARM_MOTOR", 7);
	
	int RAMP_LEFT = settingsFile.getIntProperty("RAMP_LEFT", 8);
	int RAMP_RIGHT = settingsFile.getIntProperty("RAMP_RIGHT", 9);
	//TODO Redo Ramp. (2 more ground ports, 2 rope release solenoids)
	int RAMP_RELEASE_SPEED = settingsFile.getIntProperty("RAMP_RELEASE_SPEED", 1);
	int RAMP_RETRACT_SPEED = settingsFile.getIntProperty("RAMP_RETRACT_SPEED", -1);
	
	int GRAB_SOLENOID_1 = settingsFile.getIntProperty("GRAB_SOLENOID_1", 0);
	int GRAB_SOLENOID_2 = settingsFile.getIntProperty("GRAB_SOLENOID_2", 1);
	
	int GEAR_SHIFTER_SOLENOID_1 = settingsFile.getIntProperty("GEAR_SHIFTER_SOLENOID_1", 2);
	int GEAR_SHIFTER_SOLENOID_2 = settingsFile.getIntProperty("GEAR_SHIFTER_SOLENOID_2", 3);
	
	int ARM_SOLENOID_1 = settingsFile.getIntProperty("ARM_SOLENOID_1", 4);
	int ARM_SOLENOID_2 = settingsFile.getIntProperty("ARM_SOLENOID_2", 5);
	
	int ENCODER_LEFT_1 = settingsFile.getIntProperty("ENCODER_LEFT_1", 0);
	int ENCODER_LEFT_2 = settingsFile.getIntProperty("ENCODER_LEFT_2", 1);
	int ENCODER_RIGHT_1 = settingsFile.getIntProperty("ENCODER_RIGHT_1", 2);
	int ENCODER_RIGHT_2 = settingsFile.getIntProperty("ENCODER_RIGHT_2", 3);
	
	int XBOXCONTROLLER_1 = settingsFile.getIntProperty("XBOXCONTROLLER_1", 0);
	int XBOXCONTROLLER_2 = settingsFile.getIntProperty("XBOXCONTROLLER_2", 1);

	// PID values
	// TODO get default values
	int SPEED_P = settingsFile.getIntProperty("SPEED_P", 0);
	int SPEED_I = settingsFile.getIntProperty("SPEED_I", 0);
	int SPEED_D = settingsFile.getIntProperty("SPEED_D", 0);
	
	int TURN_P = settingsFile.getIntProperty("TURN_P", 0);
	int TURN_I = settingsFile.getIntProperty("TURN_I", 0);
	int TURN_D = settingsFile.getIntProperty("TURN_D", 0);
	
}
