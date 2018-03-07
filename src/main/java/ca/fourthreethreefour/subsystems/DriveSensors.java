package main.java.ca.fourthreethreefour.subsystems;

import com.kauailabs.navx.frc.AHRS;

import main.java.ca.fourthreethreefour.identifiers.VolatileInputOutput;
import main.java.ca.fourthreethreefour.module.actuators.DualEncoderInput;
import main.java.ca.fourthreethreefour.settings.Settings;
import edu.first.identifiers.Input;
import edu.first.module.Module;
import edu.first.module.controllers.PIDController;
import edu.first.module.sensors.EncoderModule;
import edu.first.module.sensors.EncoderModule.InputType;
import edu.first.module.subsystems.Subsystem;
import edu.wpi.first.wpilibj.SPI;

public interface DriveSensors extends Settings {
    
    AHRS navx = new AHRS(SPI.Port.kMXP);
    EncoderModule leftEncoder = new EncoderModule(ENCODER_LEFT_1, ENCODER_LEFT_2, InputType.DISTANCE), 
            rightEncoder = new EncoderModule(ENCODER_RIGHT_1, ENCODER_RIGHT_2, InputType.DISTANCE);
    
    DualEncoderInput encoderInput = new DualEncoderInput(leftEncoder, rightEncoder);
    
    Input navxInput = new Input() {
        @Override
        public double get() {
            return navx.getAngle();
        }
    };
    
    VolatileInputOutput speedOutput = new VolatileInputOutput(), 
            turningOutput = new VolatileInputOutput();
    
    PIDController distancePID = new PIDController(encoderInput, speedOutput, SPEED_P, SPEED_I, SPEED_D);
    PIDController turnPID = new PIDController(navxInput, turningOutput, TURN_P, TURN_I, TURN_D);
    
    // navx is not of type Module, so it can't go here
    Subsystem encoders = new Subsystem(new Module[] { leftEncoder, rightEncoder });
    
}
