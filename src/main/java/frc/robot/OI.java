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

public class OI {
    // private XboxController controller = new XboxController(0);
    private BetterJoystick driveStick = new BetterJoystick(0, 1);
    // private BetterJoystick gunnerStick = new BetterJoystick(1, 0);

    public double velocity;
    public double rotation;

    public double armVelocity;
    public double intakeVelocity;

    public boolean ToggleArmUp;
    public boolean ToggleArmDown;
    public boolean setMid;
    public boolean goMid = false;
    public boolean ClawToggle;
    public boolean IntakeToggle;
    public boolean InvIntakeToggle;
    public boolean ToggleIntakeUp;
    public boolean ToggleIntakeDown;

    public JoystickButton armUp;
    public JoystickButton armDown;
    public JoystickButton armToggle;
    public JoystickButton clawToggle;
    public JoystickButton intakeToggle;
    public JoystickButton invIntakeToggle;
    public JoystickButton toggleIntakeUp;
    public JoystickButton toggleIntakeDown;

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
        intakeToggle = driveStick.getWPIJoystickButton("ToggleIntake");
        toggleIntakeUp = driveStick.getWPIJoystickButton("IntakeUp");
        toggleIntakeDown = driveStick.getWPIJoystickButton("IntakeDown");

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
        setMid = driveStick.getButton("GoMid");

        if (ToggleArmUp) {
            armVelocity = 0.6;
        }
        else if (ToggleArmDown) {
            armVelocity = -0.2;
        }
        else if (setMid) {
            goMid = true;
        }
        else {
            armVelocity = SmartDashboard.getNumber("IDLE_SPEED", 0.1);
        }

    }

    public boolean GoMid() {
        return goMid;
    }

    public boolean checkClaw() {
        ClawToggle = driveStick.getButton("ToggleClaw");
        return ClawToggle;
    }

    public boolean checkIntake() {
        IntakeToggle = driveStick.getButton("ToggleIntake");
        return IntakeToggle;
    }
    public boolean checkInvIntake() {
        InvIntakeToggle = driveStick.getButton("ToggleInvIntake");
        return InvIntakeToggle;
    }

    public void checkIntakeMotor() {
        ToggleIntakeUp = driveStick.getButton("IntakeUp");
        ToggleIntakeDown = driveStick.getButton("IntakeDown"); 

        if (ToggleIntakeUp) {
            intakeVelocity = 0.8;
        }
        else if (ToggleIntakeDown) {
            intakeVelocity = -0.4;
        }
        else {
            intakeVelocity = 0.0;
        }

    }

    public double getArmSpeed() {
        return armVelocity;
    }

    public double getIntakeSpeed() {
        return intakeVelocity;
    }
}
