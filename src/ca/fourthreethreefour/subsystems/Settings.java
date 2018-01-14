package ca.fourthreethreefour.subsystems;

import java.io.File;

public interface Settings {

	SettingsFile settingsFile = new SettingsFile(new File("/settings.txt"));
	
	//TODO ports
	//int EXAMPLE_PORT = settingsFile.getIntProperty("EXAMPLE_PORT", [default port])

}
