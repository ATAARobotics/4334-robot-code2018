package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ClawCommand;
import frc.robot.subsystems.Arm;

public class OI {
    private XboxController controller = new XboxController(0);

    public double velocity;
    public double rotation;

    public double armVelocity;

    public boolean armUp;
    public boolean armDown;
    public boolean setMid;
    public boolean ClawToggle;

    public void checkInputs() {
        velocity = -controller.getLeftY();
        rotation = controller.getLeftX();
      
        if (Math.abs(velocity) < 0.2) {
            velocity = 0;
        }
        if (Math.abs(rotation) < 0.2) {
            rotation = 0;
        }
    }

    public double getSpeed() {
        return velocity;
    }

    public double getRotation() {
        return rotation;
    }

    public void checkArm() {

        armUp = controller.getYButton();
        armDown = controller.getAButton();
        setMid = controller.getBButton();

        if (armUp) {
            armVelocity = 0.6;
        }
        else if (armDown) {
            armVelocity = -0.2;
        }
        else if (setMid) {
            // Arm.goToPosition();
        }
        else {
            armVelocity = SmartDashboard.getNumber("IDLE_SPEED", 0.1);
        }

    }

    public boolean checkClaw() {
        ClawToggle = controller.getXButtonPressed();
        return ClawToggle;
        // if (ClawToggle) {
            // toggleClaw();
        // }
    }

    public double getArmSpeed() {
        return armVelocity;
    }
}
