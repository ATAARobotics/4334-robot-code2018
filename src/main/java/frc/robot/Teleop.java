// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import javax.naming.directory.DirContext;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ArmCommand;
import frc.robot.commands.ArmCommandGroup;
import frc.robot.commands.ClawCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.ElbowCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Arm.ArmDirection;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
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
  private ArmCommand armCommand;
  private Drivetrain drivetrain;
  private ClawCommand clawCommand;
  private IntakeCommand intakeCommand;
  private ElbowCommand elbowCommand;
  private ArmCommandGroup armCommandGroup;
  private OI controlleroi;
  private Arm arm;
  private Claw claw;
  private Intake intake;
  private CommandScheduler commandScheduler;
  // private final Drivetrain drivetrain = new Drivetrain();
  

  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public Teleop(Drivetrain drivetrain, OI controller, Arm arm, Claw claw, Intake intake) {
    this.drivetrain = drivetrain;
    this.controlleroi = controller;
    this.arm = arm;
    this.claw = claw;
    this.intake = intake;
    driveCommand = new DriveCommand(drivetrain, controller::getSpeed, controller::getRotation);
    armCommand = new ArmCommand(arm, controller::getArmSpeed, controller::getDirection);
    clawCommand = new ClawCommand(claw, controller::checkClaw);
    intakeCommand = new IntakeCommand(intake, controller::checkIntake, controller::checkInvIntake, controller::getIntakeSpeed);
    elbowCommand = new ElbowCommand(arm, controller::getDirection);
    // TODO: Probably need to check this bottom line
    armCommandGroup = new ArmCommandGroup(armCommand, elbowCommand, 0.5, arm.armUpMotion());
    SmartDashboard.setDefaultNumber("IDLE_SPEED", 0.1);
    commandScheduler = CommandScheduler.getInstance();
    // Configure the button bindings
    configureButtonBindings();
  }


  public void teleopPeriodic() {

    controlleroi.checkInputs();
    controlleroi.checkIntakeMotor();

    // arm
    if (controlleroi.checkArm()) {
      commandScheduler.schedule(armCommandGroup);
    }
    
    // drive
    if ((Math.abs(controlleroi.getSpeed()) > 0.2) || (Math.abs(controlleroi.getRotation()) > 0.2)) {
      commandScheduler.schedule(driveCommand);
    }

    // claw
    if (controlleroi.checkClaw()) {
      commandScheduler.schedule(clawCommand);
    }

    // intake
    
    if (controlleroi.checkIntake()) {
      commandScheduler.schedule(intakeCommand);
    }

    // scehduling
    commandScheduler.run();
    

    
    SmartDashboard.putNumber("ArmSpeed", controlleroi.getArmSpeed());
    SmartDashboard.putBoolean("Y Button", controlleroi.ToggleArmUp);
    SmartDashboard.putBoolean("A Button", controlleroi.ToggleArmDown);
    SmartDashboard.putNumber("ArmPotent", arm.getPotentioValue());
    SmartDashboard.putNumber("PID Value", arm.getPIDvalue());
    SmartDashboard.putString("Claw Position", claw.ClawPosition());
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
  }
}
