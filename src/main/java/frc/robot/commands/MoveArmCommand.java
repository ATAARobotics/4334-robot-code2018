package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;

public class MoveArmCommand extends CommandBase {
    private Arm arm;
    private ArmDirection direction;

    public MoveArmCommand(Arm arm, ArmDirection direction) {
        this.arm = arm;

        this.direction = direction;

        addRequirements(this.arm);
    }

    @Override
    public void initialize() {
        arm.setElbow(direction);
    }

    @Override
    public void execute() {
        if (arm.getArmPos() != direction) {
            arm.moveArm(direction);
        }
    }

    @Override
    public boolean isFinished() {
        return arm.getArmPos() == direction;
    }
}
