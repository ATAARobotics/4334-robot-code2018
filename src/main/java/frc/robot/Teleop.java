package frc.robot;

import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Drivetrain;

public class Teleop {
    private Drivetrain drivetrain;
    private Arm arm;
    private OI joysticks;

    public Teleop(Drivetrain drivetrain, Arm arm, OI joysticks) {
        this.drivetrain = drivetrain;
        this.arm = arm;
        this.joysticks = joysticks;

        this.drivetrain
                .setDefaultCommand(new DriveCommand(drivetrain, this.joysticks::getSpeed, this.joysticks::getRotation));
    }

    public void teleopPeriodic() {
        joysticks.checkInputs();
    }
}
