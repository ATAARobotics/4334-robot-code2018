package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakeCommand extends CommandBase {
    private Intake intake;
    private BooleanSupplier triggerIntake;
    private BooleanSupplier triggerInvIntake;
    private DoubleSupplier intakeSpeed;

    public IntakeCommand(Intake intake, BooleanSupplier triggerIntake, BooleanSupplier triggerInvIntake, DoubleSupplier intakeSpeed) {
        this.intake = intake;
        this.triggerIntake = triggerIntake;
        this.triggerInvIntake = triggerInvIntake;
        this.intakeSpeed = intakeSpeed;
        addRequirements(this.intake);
    }

    @Override
    public void execute() {
        if (this.triggerIntake.getAsBoolean()) {
            intake.runIntakeWheel();
        }
        else if (this.triggerInvIntake.getAsBoolean()) {
            intake.runInvIntakeWheel();
        }
        else {
            intake.stopIntakeWheel();
        }

        intake.moveIntake(intakeSpeed.getAsDouble());
    }  
}
