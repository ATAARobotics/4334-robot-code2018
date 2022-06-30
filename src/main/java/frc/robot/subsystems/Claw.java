package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Claw extends SubsystemBase {

    public enum ClawDirection {
        OPEN,
        CLOSED
    }

    private DoubleSolenoid clawSolenoid;

    private ClawDirection currentPosition = ClawDirection.OPEN;

    public Claw() {
        clawSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.CLAW_PINCH[0],
                Constants.CLAW_PINCH[1]);

        clawSolenoid.set(Value.kReverse);
    }

    public void toggleClaw() {
        if (currentPosition == ClawDirection.OPEN) {
            clawSolenoid.set(Value.kForward);
            currentPosition = ClawDirection.CLOSED;
        } else {
            clawSolenoid.set(Value.kReverse);
            currentPosition = ClawDirection.OPEN;
        }
    }

}
