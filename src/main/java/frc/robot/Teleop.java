// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import javax.naming.directory.DirContext;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.OI;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class Teleop {
  // The robot's subsystems and commands are defined here...
  private DriveCommand driveCommand;
  private Drivetrain drivetrain;
  private OI controller;
  // private final Drivetrain drivetrain = new Drivetrain();

  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public Teleop(Drivetrain drivetrain, OI controller) {
    this.drivetrain = drivetrain;
    this.controller = controller;

    driveCommand = new DriveCommand(drivetrain, controller::getSpeed, controller::getRotation);
    // Configure the button bindings
    configureButtonBindings();
  }

  public void teleopPeriodic() {
    controller.checkInputs();
    // driveCommand.execute();
    drivetrain.setMotion(controller.velocity, controller.rotation);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {}

}
