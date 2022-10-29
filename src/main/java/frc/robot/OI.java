package frc.robot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.ClawCommand;
import frc.robot.subsystems.Arm;

public class OI {
    // private XboxController controller = new XboxController(0);
    private BetterJoystick driveStick = new BetterJoystick(0, 1);
    // private BetterJoystick gunnerStick = new BetterJoystick(1, 0);

    public double velocity;
    public double rotation;

    public double armVelocity;

    public boolean ToggleArmUp;
    public boolean ToggleArmDown;
    public boolean setMid;
    public boolean ClawToggle;

    public JoystickButton armUp;
    public JoystickButton armDown;
    public JoystickButton armToggle;
    public JoystickButton clawToggle;

    public OI() {
        try (InputStream input = new FileInputStream("/home/lvuser/deploy/bindings.properties")) {
            Properties bindings = new Properties();

            bindings.load(input);

            driveStick.configureBindings(bindings);

            input.close();
        } catch (FileNotFoundException e) {
            DriverStation.reportError("Button bindings file not found!", false);
        } catch (IOException e) {
            DriverStation.reportError("IOException on button binding file", false);
        }

        armUp = driveStick.getWPIJoystickButton("ArmUp");
        armDown = driveStick.getWPIJoystickButton("ArmDown");
        clawToggle = driveStick.getWPIJoystickButton("ToggleClaw");
    }

    public void checkInputs() {
        // velocity = -controller.getLeftY();
        // rotation = controller.getLeftX();
        velocity = -driveStick.getAnalog("Velocity");
        rotation = driveStick.getAnalog("Rotation");
      
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

        ToggleArmUp = driveStick.getButton("ArmUp");
        ToggleArmDown = driveStick.getButton("ArmDown");
        // setMid = controller.getBButton();

        if (ToggleArmUp) {
            armVelocity = 0.6;
        }
        else if (ToggleArmDown) {
            armVelocity = -0.2;
        }
        // else if (setMid) {
            // Arm.goToPosition();
        // }
        else {
            armVelocity = SmartDashboard.getNumber("IDLE_SPEED", 0.1);
        }

    }

    public boolean checkClaw() {
        ClawToggle = driveStick.getButton("ToggleClaw");
        return ClawToggle;
    }

    public double getArmSpeed() {
        return armVelocity;
    }
}
