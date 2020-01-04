package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive {
    private Spark backLeftMotor;
    private Spark frontLeftMotor;
    private Spark backRightMotor;
    private Spark frontRightMotor;
    private SpeedControllerGroup leftMotors;
    private SpeedControllerGroup rightMotors;

    private DifferentialDrive drive;

    public Drive( ) {
        backLeftMotor = new Spark(0);
        frontLeftMotor = new Spark(1);
        backRightMotor = new Spark(2);
        frontRightMotor = new Spark(3);
        leftMotors = new SpeedControllerGroup(backLeftMotor, frontLeftMotor);
        rightMotors = new SpeedControllerGroup(backRightMotor, frontRightMotor);
        drive = new DifferentialDrive(leftMotors, rightMotors);

        leftMotors.setInverted(true);
        rightMotors.setInverted(true);
    }
    public void driveInit(){
        drive.setSafetyEnabled(true);
        drive.setExpiration(0.1);
        drive.setMaxOutput(0.8);
    }
    public void arcadeDrive(double speed, double turn, boolean squared){
        drive.arcadeDrive(speed, turn, squared);

    }
}