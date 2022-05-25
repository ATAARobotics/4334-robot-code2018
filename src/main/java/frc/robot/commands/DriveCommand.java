package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveCommand extends CommandBase {
    private Drivetrain drivetrain;
    private DoubleSupplier speedSupplier;
    private DoubleSupplier rotationSupplier;

    public DriveCommand(Drivetrain drivetrain, DoubleSupplier speedSupplier, DoubleSupplier rotationSupplier) {
        this.drivetrain = drivetrain;

        this.speedSupplier = speedSupplier;
        this.rotationSupplier = rotationSupplier;

        addRequirements(this.drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.setMotion(speedSupplier.getAsDouble(), rotationSupplier.getAsDouble());
    }
}
