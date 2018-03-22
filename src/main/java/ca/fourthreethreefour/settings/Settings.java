package main.java.ca.fourthreethreefour.settings;

import java.io.File;

import main.java.ca.fourthreethreefour.module.actuators.MotorModule;
import main.java.ca.fourthreethreefour.module.actuators.MotorModule.Type;

/**
 * Contains all values {@link SettingsFile} can parse, as well as their keys and default values.
 * @author Trevor, but also Joel
 * 
 */
public interface Settings {
	
	
	public static Type toType(String type) {
		if (type.equalsIgnoreCase("talon_srx") || type.equalsIgnoreCase("talonsrx")) {
			return Type.TALON_SRX;
		}
		
		if (type.equalsIgnoreCase("victor_spx") || type.equalsIgnoreCase("victorspx")) {
			return Type.VICTOR_SPX;
		}
		
		return null;
	}

	
	SettingsFile settingsFile = new SettingsFile(new File("/settings.txt"));

    String AUTO_TYPE = settingsFile.getProperty("AUTO_TYPE", "Right");
    boolean IS_PLAYOFF = settingsFile.getBooleanProperty("IS_PLAYOFF", false);
	
    boolean LOGGING_ENABLED = settingsFile.getBooleanProperty("LOGGING_ENABLED", false);
	
    double TURN_CURVE = settingsFile.getDoubleProperty("TURN_CURVE", 1.5);

    // Ports
	// TODO get default ports
	// int EXAMPLE_PORT = settingsFile.getIntProperty("EXAMPLE_PORT", [default port])
	int DRIVE_LEFT_1 = settingsFile.getIntProperty("DRIVE_LEFT_1", 0);
	int DRIVE_LEFT_2 = settingsFile.getIntProperty("DRIVE_LEFT_2", 1);
	int DRIVE_RIGHT_1 = settingsFile.getIntProperty("DRIVE_RIGHT_1", 2);
	int DRIVE_RIGHT_2 = settingsFile.getIntProperty("DRIVE_RIGHT_2", 3);

	int GEAR_SHIFTER_SOLENOID_1 = settingsFile.getIntProperty("GEAR_SHIFTER_SOLENOID_1", 0);
	int GEAR_SHIFTER_SOLENOID_2 = settingsFile.getIntProperty("GEAR_SHIFTER_SOLENOID_2", 1);

	double LOW_GEAR_THRESHOLD = settingsFile.getDoubleProperty("LOW_GEAR_THRESHOLD", 0.1);

	int ARM_MOTOR = settingsFile.getIntProperty("ARM_MOTOR", 8);
	double ARM_SPEED = settingsFile.getDoubleProperty("ARM_SPEED", 0.5);
	
	int INTAKE_LEFT = settingsFile.getIntProperty("INTAKE_LEFT", 4);
	int INTAKE_RIGHT = settingsFile.getIntProperty("INTAKE_RIGHT", 6);
	
	MotorModule.Type TYPE_DRIVE_LEFT_1 = toType(settingsFile.getProperty("TYPE_DRIVE_LEFT_1", "talonsrx"));
	MotorModule.Type TYPE_DRIVE_LEFT_2 = toType(settingsFile.getProperty("TYPE_DRIVE_LEFT_2", "talonsrx"));
	MotorModule.Type TYPE_DRIVE_RIGHT_1 = toType(settingsFile.getProperty("TYPE_DRIVE_RIGHT_1", "talonsrx"));
	MotorModule.Type TYPE_DRIVE_RIGHT_2 = toType(settingsFile.getProperty("TYPE_DRIVE_RIGHT_2", "talonsrx"));

	MotorModule.Type TYPE_ARM_MOTOR = toType(settingsFile.getProperty("TYPE_ARM_MOTOR", "victorspx"));

	MotorModule.Type TYPE_INTAKE_LEFT = toType(settingsFile.getProperty("TYPE_INTAKE_LEFT", "talonsrx"));
	MotorModule.Type TYPE_INTAKE_RIGHT = toType(settingsFile.getProperty("TYPE_INTAKE_RIGHT", "talonsrx"));
	
	int CLAW_SOLENOID_1 = settingsFile.getIntProperty("CLAW_SOLENOID_1", 4);
	int CLAW_SOLENOID_2 = settingsFile.getIntProperty("CLAW_SOLENOID_2", 5);
	
	int FLEX_SOLENOID_1 = settingsFile.getIntProperty("FLEX_SOLENOID_1", 6);
	int FLEX_SOLENOID_2 = settingsFile.getIntProperty("FLEX_SOLENOID_2", 7);
	
	int ENCODER_LEFT_1 = settingsFile.getIntProperty("ENCODER_LEFT_1", 0);
	int ENCODER_LEFT_2 = settingsFile.getIntProperty("ENCODER_LEFT_2", 1);
	int ENCODER_RIGHT_1 = settingsFile.getIntProperty("ENCODER_RIGHT_1", 2);
	int ENCODER_RIGHT_2 = settingsFile.getIntProperty("ENCODER_RIGHT_2", 3);
	int POTENTIOMETER = settingsFile.getIntProperty("POTENTIOMETER", 0);
	
	int XBOXCONTROLLER_1 = settingsFile.getIntProperty("XBOXCONTROLLER_1", 0);
	int XBOXCONTROLLER_2 = settingsFile.getIntProperty("XBOXCONTROLLER_2", 1);

	// PID values
	// TODO get default values
	double SPEED_P = settingsFile.getDoubleProperty("SPEED_P", 0);
	double SPEED_I = settingsFile.getDoubleProperty("SPEED_I", 0);
	double SPEED_D = settingsFile.getDoubleProperty("SPEED_D", 0);

	double TURN_P = settingsFile.getDoubleProperty("TURN_P", 0);
	double TURN_I = settingsFile.getDoubleProperty("TURN_I", 0);
	double TURN_D = settingsFile.getDoubleProperty("TURN_D", 0);
	
	double ARM_P = settingsFile.getDoubleProperty("ARM_P", 2.5);
	double ARM_I = settingsFile.getDoubleProperty("ARM_I", 0);
	double ARM_D = settingsFile.getDoubleProperty("ARM_D", 3);

	// absolute highest position of the arm, all PID setpoints are relative to this
	double ARM_PID_TOP = settingsFile.getDoubleProperty("ARM_PID_TOP", 0.797119);
	double ARM_PID_LOW = ARM_PID_TOP - settingsFile.getDoubleProperty("ARM_PID_LOW", 0.6);
	double ARM_PID_MEDIUM = ARM_PID_TOP - settingsFile.getDoubleProperty("ARM_PID_MEDIUM", 0.51);
	double ARM_PID_HIGH = ARM_PID_TOP - settingsFile.getDoubleProperty("ARM_PID_HIGH", 0.02);
	double ARM_ANGLE_MIN = settingsFile.getDoubleProperty("ARM_ANGLE_MIN", 0.25);
	double ARM_ANGLE_MAX = settingsFile.getDoubleProperty("ARM_ANGLE_MAX", 0.73);

	double DISTANCE_TOLERANCE = settingsFile.getDoubleProperty("DISTANCE_TOLERANCE", 50);
	double TURN_TOLERANCE = settingsFile.getDoubleProperty("TURN_TOLERANCE", 5);
}
