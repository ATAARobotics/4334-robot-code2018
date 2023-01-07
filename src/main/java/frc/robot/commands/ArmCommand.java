package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class ArmCommand extends CommandBase{
    private Arm arm;
    private DoubleSupplier speedSupplier;
    private Supplier<Arm.ArmDirection> armDirection;
    private double speedConst = 0.2;


    public ArmCommand(Arm arm, DoubleSupplier speedSupplier, Supplier<Arm.ArmDirection> armDirection) { // add supplier to armDirection
        this.arm = arm;
        this.speedSupplier = speedSupplier;
        this.armDirection = armDirection;
        addRequirements(this.arm);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        SmartDashboard.putString("currPos", arm.getArmPos().toString());
        arm.checkPosition(armDirection.get());
        arm.setElbow(armDirection.get());
        arm.goToPosition();
        arm.moveArm(clamp(speedConst * arm.getPIDvalue(), -0.2, 0.5)); //speedSupplier.getAsDouble()

        arm.changePID();
        
    }

    private double clamp(double d, double i, double j) {
        return Math.max(i, Math.min(j, d));
    }
}
