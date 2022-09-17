package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class OI {
    private XboxController controller = new XboxController(0);

    public double velocity;
    public double rotation;



    public void checkInputs() {
        velocity = -controller.getRightY();
        rotation = controller.getRightX();

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
}
