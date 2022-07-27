package frc.robot.subsystems;

import java.util.Arrays;

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
    private int moveLightCount = 0;
    private int offset = 0;
    private int[][] colorList;

    public LightingSubsystem() {
        candle = new CANdle(2);
        config = new CANdleConfiguration();
        config.stripType = LEDStripType.RGB;
        config.brightnessScalar = 1;
        candle.configAllSettings(config);

        colorList = generateGradient(20, new int[] { 255, 0, 0 }, new int[] { 0, 80, 0 },
                new int[] { 0, 0, 20 });

        candle.setLEDs(0, 0, 0);
    }

    private int[][] generateGradient(int step, int[]... colors) {
        int[][] newColorList = new int[step * colors.length / 2][3];
        for (int color = 0; color < colors.length; color++) {
            int[] thisColor = colors[color];
            int[] nextColor;
            if (color != colors.length - 1) {
                nextColor = colors[color + 1];
            } else {
                nextColor = colors[0];
            }
            int[] distance = { nextColor[0] - thisColor[0], nextColor[1] - thisColor[1], nextColor[2] - thisColor[2] };

            for (int i = 0; i < step; i += 2) {
                newColorList[color * step + i] = new int[] {
                        (int) ((double) thisColor[0] + ((double) distance[0] * ((double) i / (double) step))),
                        (int) ((double) thisColor[1] + ((double) distance[1] * ((double) i / (double) step))),
                        (int) ((double) thisColor[2] + ((double) distance[2] * ((double) i / (double) step)))
                };
            }
        }
        return newColorList;
    }

    public void lightUp(double lightPercent) {
        moveLightCount++;
        if (moveLightCount == 5) {
            moveLightCount = 0;
            offset++;
            offset %= colorList.length;
        }

        int count = (int) Math.floor(50 * lightPercent);

        candle.setLEDs(0, 0, 0, 0, 8 + count, 50 - count);
        candle.setLEDs(0, 0, 0, 0, 78, 50 - count);

        for (int i = 0; i < count; i += 2) {
            candle.setLEDs(colorList[((i - offset + colorList.length) % colorList.length) / 2][0],
                    colorList[((i - offset + colorList.length) % colorList.length) / 2][1],
                    colorList[((i - offset + colorList.length) % colorList.length) / 2][2], 0, i + 8, 2);
            candle.setLEDs(colorList[((i - offset + colorList.length) % colorList.length) / 2][0],
                    colorList[((i - offset + colorList.length) % colorList.length) / 2][1],
                    colorList[((i - offset + colorList.length) % colorList.length) / 2][2], 0, 127 - i - 1, 2);
        }
    }
}