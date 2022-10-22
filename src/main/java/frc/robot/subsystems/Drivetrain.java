// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {
  // initialize variables
    private VictorSPX frontLeftMotor;
    private VictorSPX frontRightMotor;
    private VictorSPX backLeftMotor;
    private VictorSPX backRightMotor;
  
  
  public Drivetrain() {
    // where the magic happens
    frontLeftMotor = new VictorSPX(Constants.LEFT_DRIVE_MOTORS[0]);
    frontRightMotor = new VictorSPX(Constants.RIGHT_DRIVE_MOTORS[0]);
    backLeftMotor = new VictorSPX(Constants.LEFT_DRIVE_MOTORS[1]);
    backRightMotor = new VictorSPX(Constants.RIGHT_DRIVE_MOTORS[1]);

    backLeftMotor.follow(frontLeftMotor);
    backRightMotor.follow(frontRightMotor);

    frontLeftMotor.setInverted(true);
    backLeftMotor.setInverted(true);
  }

  public void setMotion(double speed, double rotation) {
    frontLeftMotor.set(ControlMode.PercentOutput, MathUtil.clamp(speed + rotation, -1.0, 1.0));
    frontRightMotor.set(ControlMode.PercentOutput, MathUtil.clamp(speed - rotation, -1.0, 1.0));
  }
}
