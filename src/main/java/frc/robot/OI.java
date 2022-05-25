package frc.robot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.wpi.first.wpilibj.DriverStation;

public class OI {

    private BetterJoystick driveStick = new BetterJoystick(0, 0);

    public double velocity;
    public double rotation;

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
    }

    public void checkInputs() {
        velocity = -driveStick.getAnalog("Velocity");
        rotation = driveStick.getAnalog("Rotation");
    }

    public double getSpeed() {
        return velocity;
    }

    public double getRotation() {
        return rotation;
    }
}
