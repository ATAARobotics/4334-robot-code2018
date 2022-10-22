package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;

public class ArmCommand extends CommandBase{
    private Arm arm;
    private DoubleSupplier speedSupplier;

    public ArmCommand(Arm arm, DoubleSupplier speedSupplier) {
        this.arm = arm;
        this.speedSupplier = speedSupplier;
        addRequirements(this.arm);
    }

    @Override
    public void initialize() {
        // if (arm.getArmMotion() == ArmDirection.DOWN) {
        //     direction = ArmDirection.UP;
        // } else {
        //     direction = ArmDirection.DOWN;
        // }

        // arm.setElbow(direction);
    }

    @Override
    public void execute() {
        // System.out.println(direction.toString());
        SmartDashboard.putString("currPos", arm.getArmPos().toString());
        // SmartDashboard.putString("targetPos", direction.toString());

        arm.moveArm(speedSupplier.getAsDouble());
        
        // if (arm.getArmPos() != direction) {
            // arm.moveArm(direction);
        // }
    }
}
