package main.java.ca.fourthreethreefour.settings;

import java.io.File;

/**
 * Contains all values {@link SettingsFile} can parse, as well as their keys and default values.
 * @author Trevor, but also Joel
 * 
 */
public interface Settings {

	SettingsFile settingsFile = new SettingsFile(new File("/settings.txt"));

	String ROBOT_TYPE = settingsFile.getProperty("ROBOT_TYPE", "");
    String AUTO_TYPE = settingsFile.getProperty("AUTO_TYPE", "");
	
    boolean LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);
	
    double TURN_CURVE = settingsFile.getDoubleProperty("TURN_CURVE", 1.5);
    double TURN_CONSTANT = settingsFile.getDoubleProperty("TURN_CONSTANT", 1);
    // Ports
	// TODO get default ports
	// int EXAMPLE_PORT = settingsFile.getIntProperty("EXAMPLE_PORT", [default port])
	int DRIVE_LEFT_1 = settingsFile.getIntProperty("DRIVE_LEFT_1", 0);
	int DRIVE_LEFT_2 = settingsFile.getIntProperty("DRIVE_LEFT_2", 1);
	int DRIVE_RIGHT_1 = settingsFile.getIntProperty("DRIVE_RIGHT_1", 2);
	int DRIVE_RIGHT_2 = settingsFile.getIntProperty("DRIVE_RIGHT_2", 3);
	
	int GEAR_SHIFTER_SOLENOID_1 = settingsFile.getIntProperty("GEAR_SHIFTER_SOLENOID_1", 4);
	int GEAR_SHIFTER_SOLENOID_2 = settingsFile.getIntProperty("GEAR_SHIFTER_SOLENOID_2", 5);
	
	int ARM_MOTOR = settingsFile.getIntProperty("ARM_MOTOR", 8);
	double ARM_SPEED = settingsFile.getDoubleProperty("ARM_SPEED", 1);
	
	int RAMP_LEFT_1 = settingsFile.getIntProperty("RAMP_LEFT_1", 4);
	int RAMP_LEFT_2 = settingsFile.getIntProperty("RAMP_LEFT_2", 5);
	int RAMP_RIGHT_1 = settingsFile.getIntProperty("RAMP_RIGHT_1", 6);
	int RAMP_RIGHT_2 = settingsFile.getIntProperty("RAMP_RIGHT_2", 7);
	
	int RAMP_RELEASE_LEFT = settingsFile.getIntProperty("RAMP_RELEASE_LEFT", 6);
	int RAMP_RELEASE_RIGHT = settingsFile.getIntProperty("RAMP_RELEASE_RIGHT", 7);
	
	double RAMP_RETRACT_SPEED = settingsFile.getDoubleProperty("RAMP_RETRACT_SPEED", -0.1);
	
	int CLAW_SOLENOID_1 = settingsFile.getIntProperty("CLAW_SOLENOID_1", 0);
	int CLAW_SOLENOID_2 = settingsFile.getIntProperty("CLAW_SOLENOID_2", 1);
	
	int FLEX_SOLENOID_1 = settingsFile.getIntProperty("FLEX_SOLENOID_1", 2);
	int FLEX_SOLENOID_2 = settingsFile.getIntProperty("FLEX_SOLENOID_2", 3);
	
	int ENCODER_LEFT_1 = settingsFile.getIntProperty("ENCODER_LEFT_1", 0);
	int ENCODER_LEFT_2 = settingsFile.getIntProperty("ENCODER_LEFT_2", 1);
	int ENCODER_RIGHT_1 = settingsFile.getIntProperty("ENCODER_RIGHT_1", 2);
	int ENCODER_RIGHT_2 = settingsFile.getIntProperty("ENCODER_RIGHT_2", 3);
	int POTENTIOMETER = settingsFile.getIntProperty("POTENTIOMETER", 0);
	
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
	
	double ARM_P = settingsFile.getDoubleProperty("ARM_P", 2.5);
	double ARM_I = settingsFile.getDoubleProperty("ARM_I", 0);
	double ARM_D = settingsFile.getDoubleProperty("ARM_D", 3);
	
	// TODO get default values for all below
	double ARM_ANGLE_MIN = settingsFile.getDoubleProperty("ARM_ANGLE_MIN", 0.25);
	double ARM_ANGLE_MAX = settingsFile.getDoubleProperty("ARM_ANGLE_MAX", 0.73);
	
	double ARM_PID_START = settingsFile.getDoubleProperty("ARM_PID_START", 0.0); // Might end up being the
	
	double ARM_PID_LOW = settingsFile.getDoubleProperty("ARM_PID_LOW", 0.5); // same?
	double ARM_PID_MEDIUM = settingsFile.getDoubleProperty("ARM_PID_MEDIUM", 0.0);
	double ARM_PID_HIGH = settingsFile.getDoubleProperty("ARM_PID_HIGH", 0.0);
	
}
