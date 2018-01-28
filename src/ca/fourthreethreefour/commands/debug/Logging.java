package ca.fourthreethreefour.commands.debug;

import ca.fourthreethreefour.settings.Settings;

public class Logging implements Settings {

    public static void put(String key, double value) {
        if (LOGGING_ENABLED) {
            edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putNumber(key, value);
        }
    }
    
    public static void put(String key, String value) {
        if (LOGGING_ENABLED) {
            edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putString(key, value);
        }
    }
    
    public static void put(String key, boolean value) {
        if (LOGGING_ENABLED) {
            edu.wpi.first.wpilibj.smartdashboard.SmartDashboard.putBoolean(key, value);
        }
    }
    
    public static void log(String str) {
        if (LOGGING_ENABLED) {
            System.out.println(str);
        }
    }
    
    public static void logf(String format, Object... args) {
        if (LOGGING_ENABLED) {
            System.out.printf(format + "\n", args);
        }
    }
}
