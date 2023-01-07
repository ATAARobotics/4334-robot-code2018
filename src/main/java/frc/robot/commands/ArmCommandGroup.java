package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ArmCommandGroup extends SequentialCommandGroup{
    public ArmCommandGroup(ArmCommand armCommand, ElbowCommand elbowCommand, double wait, boolean armFirst) {
        if (armFirst) {
            addCommands(armCommand, new WaitCommand(wait), elbowCommand);
        }
        else {
            addCommands(elbowCommand, new WaitCommand(wait), armCommand);
        } 
    }
}
