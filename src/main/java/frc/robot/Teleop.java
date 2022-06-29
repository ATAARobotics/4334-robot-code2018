package frc.robot;

import frc.robot.commands.DriveCommand;
import frc.robot.commands.ToggleArmCommand;
import frc.robot.commands.ToggleClawCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Drivetrain;

public class Teleop {
    private Drivetrain drivetrain;
    private Arm arm;
    private Claw claw;
    private OI joysticks;

    public Teleop(Drivetrain drivetrain, Arm arm, Claw claw, OI joysticks) {
        this.drivetrain = drivetrain;
        this.arm = arm;
        this.claw = claw;
        this.joysticks = joysticks;

        this.drivetrain
                .setDefaultCommand(new DriveCommand(drivetrain, this.joysticks::getSpeed, this.joysticks::getRotation));

        configureBindings();
    }

    public void teleopPeriodic() {
        joysticks.checkInputs();

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
