package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class ElbowCommand extends CommandBase{
    private Arm arm;
    private Supplier<Arm.ArmDirection> armDirection;

    public ElbowCommand(Arm arm, Supplier<Arm.ArmDirection> armDirection) {
        this.arm = arm;
        this.armDirection = armDirection;
        addRequirements(this.arm);
    }

    @Override
    public void execute() {    
        arm.setElbow(armDirection.get()); 
        
    }
}
