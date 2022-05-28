package frc.robot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class OI {

    private BetterJoystick driveStick = new BetterJoystick(0, 0);

    public double velocity;
    public double rotation;

    public JoystickButton armToggle;
    public JoystickButton clawToggle;

    public OI() {
        // Configure the button bindings
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

        armToggle = driveStick.getWPIJoystickButton("ToggleArm");
        clawToggle = driveStick.getWPIJoystickButton("ToggleClaw");
    }

    public void checkInputs() {
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
}
