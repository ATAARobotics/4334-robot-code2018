// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

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
    

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
