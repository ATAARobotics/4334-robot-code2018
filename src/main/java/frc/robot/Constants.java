package frc.robot;

public class Constants {
    // Motors
    public static final int[] LEFT_DRIVE_MOTORS = { 0, 1 };
    public static final int[] RIGHT_DRIVE_MOTORS = { 2, 3 };
    public static final int INTAKE_ARM = 4;
    public static final int[] INTAKE_WHEEL = { 5, 6 };
    public static final int ARM_MOTOR = 7;

    // Pneumatics
    public static final int[] GEAR_SHIFT = { 0, 1 };
    public static final int[] ARM_ELBOW = { 4, 5 };
    public static final int[] CLAW_PINCH = { 6, 7 };

    // Sensors
    public static final int ARM_POTENTIOMETER = 1;
    public static final double ARM_DOWN_POS = 0.46;
    public static final double ARM_UP_POS = 0.91;
    public static final double ARM_TOLERANCE = 0.1;
    public static final double ARM_DOWN_SPEED = -0.2;

    // Multiply potentiometer readings by this value to keep it in a decently high
    // range to avoid floating-point stuff
    public static final double PID_SCALE_FACTOR = 1000;
}