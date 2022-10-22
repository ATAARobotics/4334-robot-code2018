package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Claw extends SubsystemBase{
    public enum ClawAction {
        OPEN,
        CLOSED
    }

    private DoubleSolenoid clawSolenoid; 
    private ClawAction currentPosition = ClawAction.OPEN;

    public Claw() {
        clawSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.CLAW_PINCH[0], Constants.CLAW_PINCH[1]);

        clawSolenoid.set(Value.kReverse);
    }

    public void toggleClaw() {
        if (currentPosition == ClawAction.OPEN) {
            clawSolenoid.set(Value.kForward);
            currentPosition = ClawAction.CLOSED;
        } else {
            clawSolenoid.set(Value.kReverse);
            currentPosition = ClawAction.OPEN;
        }
    }

    public String ClawPosition() {
        return currentPosition.name();
    }

}
