package frc.robot;

import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.Drivetrain;

public class Teleop {
    private Drivetrain drivetrain;
    private OI joysticks;

    public Teleop(Drivetrain drivetrain, OI joysticks) {
        this.drivetrain = drivetrain;
        this.joysticks = joysticks;

        this.drivetrain
                .setDefaultCommand(new DriveCommand(drivetrain, this.joysticks::getSpeed, this.joysticks::getRotation));
    }

    public void teleopPeriodic() {
        joysticks.checkInputs();
    }
}
