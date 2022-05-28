package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Claw;

public class ToggleClawCommand extends CommandBase {

    private Claw claw;

    public ToggleClawCommand(Claw claw) {
        this.claw = claw;

        addRequirements(this.claw);
    }

    @Override
    public void initialize() {
        claw.toggleClaw();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
