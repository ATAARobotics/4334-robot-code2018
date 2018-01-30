package ca.fourthreethreefour.subsystems;

import com.kauailabs.navx.frc.AHRS;

import ca.fourthreethreefour.settings.Settings;
import edu.first.module.Module;
import edu.first.module.sensors.EncoderModule;
import edu.first.module.sensors.EncoderModule.InputType;
import edu.first.module.subsystems.Subsystem;
import edu.wpi.first.wpilibj.SPI;

public interface DriveSensors extends Settings {
	
	AHRS navX = new AHRS(SPI.Port.kMXP);
	EncoderModule leftEncoder = new EncoderModule(ENCODER_LEFT_1, ENCODER_LEFT_2, InputType.DISTANCE), 
			rightEncoder = new EncoderModule(ENCODER_RIGHT_1, ENCODER_RIGHT_2, InputType.DISTANCE);
	
	// navX is not of type Module, so it can't go here
	Subsystem encoders = new Subsystem(new Module[] { leftEncoder, rightEncoder });
}
