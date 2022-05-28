package frc.robot;

public class RobotMap {
    // Motors
    public static final int[] LEFT_DRIVE_MOTORS = { 0, 1 };
    public static final int[] RIGHT_DRIVE_MOTORS = { 2, 3 };
    public static final int ARM_MOTOR = 7;

    // Pneumatics
    public static final int[] GEAR_SHIFT = { 0, 1 };
    public static final int[] ARM_ELBOW = { 6, 7 };
    public static final int[] CLAW_PINCH = { 4, 5 };

    // Sensors
    public static final int ARM_POTENTIOMETER = 1;
    public static final double ARM_DOWN_POS = -999.99;
    public static final double ARM_UP_POS = 999.99;
}
