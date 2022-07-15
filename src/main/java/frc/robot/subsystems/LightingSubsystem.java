package frc.robot.subsystems;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.CANdle.LEDStripType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LightingSubsystem extends SubsystemBase {
    private CANdle candle;
    private RainbowAnimation rainbowAnimation;
    private RainbowAnimation rainbowAnimationTop;
    private CANdleConfiguration config;
    public LightingSubsystem() {
        candle = new CANdle(2);
        config = new CANdleConfiguration();
        config.stripType = LEDStripType.RGB;
        config.brightnessScalar = 1;
        candle.configAllSettings(config);
    }
    public void lightUp(double lightPercent) {
        int count = (int) Math.floor(50*lightPercent);
        candle.setLEDs(0, 0, 0, 0, 8+count, 50-count);
        candle.setLEDs(0, 0, 0, 0, 78, 50-count);
        candle.setLEDs(0, (int) Math.round(230*lightPercent)+25, (int) Math.round(230*(-lightPercent+1))+25, 0, 8, count);
        candle.setLEDs(0, (int) Math.round(230*lightPercent)+25, (int) Math.round(230*(-lightPercent+1))+25, 0, 128-count, count);
    }

}