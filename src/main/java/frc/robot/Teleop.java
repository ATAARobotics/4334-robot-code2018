package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.ToggleArmCommand;
import frc.robot.commands.ToggleClawCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LightingSubsystem;

public class Teleop {
    private Drivetrain drivetrain;
    private Arm arm;
    private Claw claw;
    private OI joysticks; 
    private LightingSubsystem lightingSubsystem;

    public Teleop(Drivetrain drivetrain, Arm arm, Claw claw, OI joysticks, LightingSubsystem lightingSubsystem) {
        this.drivetrain = drivetrain;
        this.arm = arm;
        this.claw = claw;
        this.joysticks = joysticks;
        this.lightingSubsystem = lightingSubsystem;

        this.drivetrain
                .setDefaultCommand(new DriveCommand(drivetrain, this.joysticks::getSpeed, this.joysticks::getRotation));

        configureBindings();
    }

    public void teleopPeriodic() {
        joysticks.checkInputs();
        lightingSubsystem.lightUp(arm.getArmPercent());
        arm.periodic();
    }

    private void configureBindings() {
        joysticks.armToggle
                .whenPressed(
                        new ToggleArmCommand(arm));

        joysticks.clawToggle
                .whenPressed(
                        new ToggleClawCommand(claw));
    }
}
