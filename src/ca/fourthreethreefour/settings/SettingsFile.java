package ca.fourthreethreefour.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Reads the specified .txt file, parses the values in said .txt file, then returns those values to be used
 * in the rest of the code.
 * @author Trevor, but probably Joel actually
 *
 */
public class SettingsFile extends Properties {
	//this line only exists so Eclipse would shut up about it
	private static final long serialVersionUID = -6308390915164135156L;
	
	DriverStation driverStation = DriverStation.getInstance();
	
	/**
	 * Specifies a file to load as the settings file.
	 * @param file The settings file to parse settings from.
	 */
	public SettingsFile(File file) {
		try {
			load(new FileInputStream(file));
		} catch (FileNotFoundException fileNotFound) {
			DriverStation.reportError("File not found", false);
		} catch (IOException IOException) {
			DriverStation.reportWarning("I/O Exception", false);
		}
	}
	
	/**
	 * Returns an int specified by the "key" string in the settings file. If the value of the key 
	 * cannot be found, returns the default value specified in the constructor.
	 * @param key The string used to locate the value
	 * @param defaultValue The value returned if no value is found
	 * @return Value read from the settings file, or the default value if the specified key cannot be found
	 */
	int getIntProperty(String key, int defaultValue) {
		if (stringPropertyNames().contains(key)) {
			return Integer.parseInt(getProperty(key));
		} else {
			return defaultValue;
		}
	}
	
	/**
	 * Returns a double specified by the "key" string in the settings file. If the value of the key 
	 * cannot be found, returns the default value specified in the constructor.
	 * @param key The string used to locate the value
	 * @param defaultValue The value returned if no value is found
	 * @return Value read from the settings file, or the default value if the specified key cannot be found
	 */
	double getDoubleProperty(String key, double defaultValue) {
		if (stringPropertyNames().contains(key)) {
			return Double.parseDouble(getProperty(key));
		} else {
			return defaultValue;
		}
	}

	/**
	 * Returns a boolean specified by the "key" string in the settings file. If the value of the key 
	 * cannot be found, returns the default value specified in the constructor.
	 * @param key The string used to locate the value
	 * @param defaultValue The value returned if no value is found
	 * @return Value read from the settings file, or the default value if the specified key cannot be found
	 */
	boolean getBooleanProperty(String key, boolean defaultValue) {
		if (stringPropertyNames().contains(key)) {
			return Boolean.parseBoolean(getProperty(key));
		} else {
			return defaultValue;
		}
	}
}
