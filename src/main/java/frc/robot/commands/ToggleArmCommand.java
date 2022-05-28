package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;

public class ToggleArmCommand extends CommandBase {
    private Arm arm;
    private ArmDirection direction;

    public ToggleArmCommand(Arm arm) {
        this.arm = arm;

        addRequirements(this.arm);

    }

    @Override
    public void initialize() {
        if (arm.getArmMotion() == ArmDirection.DOWN) {
            direction = ArmDirection.UP;
        } else {
            direction = ArmDirection.DOWN;
        }

        arm.setElbow(direction);
    }

    @Override
    public void execute() {
        SmartDashboard.putString("curPos", arm.getArmPos().toString());
        SmartDashboard.putString("targetPos", direction.toString());
        if (arm.getArmPos() != direction) {
            arm.moveArm(direction);
        }
    }

    @Override
    public void end(boolean interrupted) {
        arm.stopArm();
    }

    @Override
    public boolean isFinished() {
        return arm.getArmPos() == direction;
    }
}
