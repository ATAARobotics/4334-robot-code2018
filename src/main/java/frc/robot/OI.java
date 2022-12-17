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
import frc.robot.subsystems.Arm;

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
    public boolean ClawToggle;
    public boolean IntakeToggle;
    public boolean InvIntakeToggle;
    public boolean ToggleIntakeUp;
    public boolean ToggleIntakeDown;


    // PID values
    public Arm.ArmDirection armMotion;

    // PID Booleans
    public boolean GoDown;
    public boolean GoMid;
    public boolean GoUp;

    public JoystickButton armUp;
    public JoystickButton armDown;
    public JoystickButton armToggle;
    public JoystickButton clawToggle;
    public JoystickButton intakeToggle;
    public JoystickButton invIntakeToggle;
    public JoystickButton toggleIntakeUp;
    public JoystickButton toggleIntakeDown;

    // PID Buttons
    public JoystickButton goDown;
    public JoystickButton goMid;
    public JoystickButton goUp;

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

        // armUp = driveStick.getWPIJoystickButton("ArmUp");
        // armDown = driveStick.getWPIJoystickButton("ArmDown");
        clawToggle = driveStick.getWPIJoystickButton("ToggleClaw");
        intakeToggle = driveStick.getWPIJoystickButton("ToggleIntake");
        toggleIntakeUp = driveStick.getWPIJoystickButton("IntakeUp");
        toggleIntakeDown = driveStick.getWPIJoystickButton("IntakeDown");

        // PID Buttons
        goDown = driveStick.getWPIJoystickButton("GoDown");
        goMid = driveStick.getWPIJoystickButton("GoMid");
        goUp = driveStick.getWPIJoystickButton("GoUp");

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

        // ToggleArmUp = driveStick.getButton("ArmUp");
        // ToggleArmDown = driveStick.getButton("ArmDown");

        // if (ToggleArmUp) {
        //     armVelocity = 0.6;
        // }
        // else if (ToggleArmDown) {
        //     armVelocity = -0.2;
        // }
        // else {
        //     armVelocity = SmartDashboard.getNumber("IDLE_SPEED", 0.1);
        // }

        GoDown = driveStick.getButton("GoDown");
        GoMid = driveStick.getButton("GoMid");
        GoUp = driveStick.getButton("GoUp");

        if (GoDown) {
            armMotion = Arm.ArmDirection.DOWN;
        }
        if (GoMid) {
            armMotion = Arm.ArmDirection.MID;
        }
        if (GoUp) {
            armMotion = Arm.ArmDirection.UP;
        }

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

    public Arm.ArmDirection getDirection() {
        return armMotion;
    }
}
