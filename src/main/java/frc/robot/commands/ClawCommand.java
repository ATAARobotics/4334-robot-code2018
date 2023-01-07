package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.subsystems.Claw;
public class ClawCommand extends CommandBase{
    private Claw claw;
    private BooleanSupplier triggerClaw;

    public ClawCommand(Claw claw, BooleanSupplier triggerClaw) {
        this.triggerClaw = triggerClaw;
        this.claw = claw;

        addRequirements(this.claw);
    }

    @Override
    public void execute() {
        if (this.triggerClaw.getAsBoolean()) {
            claw.toggleClaw();
        }
    }    
}
