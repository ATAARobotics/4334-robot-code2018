package main.java.ca.fourthreethreefour.subsystems;

import edu.first.identifiers.InversedSpeedController;
import edu.first.module.Module;
import edu.first.module.actuators.SolenoidModule;
import edu.first.module.actuators.SpeedController;
import edu.first.module.actuators.SpeedControllerGroup;
import edu.first.module.subsystems.Subsystem;
import main.java.ca.fourthreethreefour.module.actuators.MotorModule;
import main.java.ca.fourthreethreefour.settings.Settings;

public interface Ramp extends Settings {
    
    /**
     * Allows TalonSRXModule's to be controlled using SpeedControllers commands, but considered
     * as a Module for Subsystem.
     * @author Cool, with major credit to Joel.
     *
     */
    
    MotorModule
        leftRamp1 = new MotorModule(TYPE_RAMP_LEFT_1, RAMP_LEFT_1),
        leftRamp2 = new MotorModule(TYPE_RAMP_LEFT_2, RAMP_LEFT_2),
        rightRamp1 = new MotorModule(TYPE_RAMP_RIGHT_1, RAMP_RIGHT_1),
        rightRamp2 = new MotorModule(TYPE_RAMP_RIGHT_2, RAMP_RIGHT_2);

    SpeedControllerGroup
        leftRamp = new SpeedControllerGroup(new SpeedController[] {
                new InversedSpeedController(leftRamp1), leftRamp2 }),
        rightRamp = new SpeedControllerGroup(new SpeedController[] {
                new InversedSpeedController(rightRamp1), rightRamp2 });
    
    SolenoidModule // Creates single action solenoids
        leftRelease = new SolenoidModule(RAMP_RELEASE_LEFT),
        rightRelease = new SolenoidModule(RAMP_RELEASE_RIGHT);

    Subsystem ramp = new Subsystem(new Module[] {
            leftRamp1, leftRamp2,
            rightRamp1, rightRamp2,
            leftRelease, rightRelease
    });
    
}
