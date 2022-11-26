package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakeCommand extends CommandBase {
    private Intake intake;
    private BooleanSupplier triggerIntake;
    private BooleanSupplier triggerInvIntake;

    public IntakeCommand(Intake intake, BooleanSupplier triggerIntake, BooleanSupplier triggerInvIntake) {
        this.intake = intake;
        this.triggerIntake = triggerIntake;
        this.triggerInvIntake = triggerInvIntake;
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
    }  
}
