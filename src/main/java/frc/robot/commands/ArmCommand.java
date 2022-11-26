package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;

public class ArmCommand extends CommandBase{
    private Arm arm;
    private DoubleSupplier speedSupplier;
    private double speedConst = 1.0;

    // ToDo
    public ArmCommand(Arm arm, DoubleSupplier speedSupplier) {//, Arm.ArmDirection armDirection) { // add supplier to armDirection
        this.arm = arm;
        this.speedSupplier = speedSupplier;
        addRequirements(this.arm);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        SmartDashboard.putString("currPos", arm.getArmPos().toString());
        // SmartDashboard.putString("targetPos", direction.toString());
        arm.goToPosition();

        arm.moveArm(speedSupplier.getAsDouble());//speedConst * arm.getPIDvalue());

        
    }
}
