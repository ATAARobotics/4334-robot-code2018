package frc.robot;

import java.util.Properties;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;


/**
 * A better interface for Xbox controllers
 */
public class BetterJoystick {

    private GenericHID controller = null;
    private Properties bindings = null;

    private int joyType;
    
    /**
     * Creates a new BetterJoystick using the given joystick port.
     * 
     * @param port The joystick port that the controller to use is plugged into
     * @param joyType The type of joystick - Xbox=0, Joystick=1
     */
    public BetterJoystick(int port, int joyType) {
        this.joyType = joyType;

        controller = new GenericHID(port);
    }

    /**
     * Check whether a given button has been activated
     * 
     * @param action The action name (not the button name) to check for - these are configured by the bindings provided
     */
    public boolean getButton(String action) {
        if (bindings == null) {
            DriverStation.reportError("Button bindings not configured yet!", false);
            return false;
        }

        String button = bindings.getProperty(action, "None");

        if (button.equals("None")) {
            return false;
        }

        String[] buttonInfo = button.split("-");

        if (buttonInfo.length != 2) {
            DriverStation.reportError("There does not appear to be exactly two arguments in the button " + button, false);
            return false;
        }

        int buttonID;
        if (joyType == 0) {
            switch (buttonInfo[0]) {
                case "X":
                    buttonID = 3;
                    break;
                
                case "Y":
                    buttonID = 4;
                    break;

                case "A":
                    buttonID = 1;
                    break;
                
                case "B":
                    buttonID = 2;
                    break;

                case "LeftBumper":
                    buttonID = 5;
                    break;

                case "RightBumper":
                    buttonID = 6;
                    break;

                case "Start":
                    buttonID = 8;
                    break;

                case "Back":
                    buttonID = 7;
                    break;

                case "LeftJoystick":
                    buttonID = 9;
                    break;

                case "RightJoystick":
                    buttonID = 10;
                    break;
            
                default:
                    DriverStation.reportError("There is no button with the name " + buttonInfo[0], false);
                    return false;
            }
        } else if (joyType == 1) {
            buttonID = Integer.parseInt(buttonInfo[0]);
        } else {
            DriverStation.reportError("There is no joystick type of " + joyType, false);
            return false;
        }

        switch (buttonInfo[1]) {
            case "Held":
                return controller.getRawButton(buttonID);

            case "Pressed":
                return controller.getRawButtonPressed(buttonID);

            case "Released":
                return controller.getRawButtonReleased(buttonID);

            default:
                DriverStation.reportError("There is no button query type with the name " + buttonInfo[1], false);
                return false;
        }
    }

    /**
     * Get the value of a given analog trigger
     * 
     * @param action The action name (not the trigger name) to get - these are configured by the bindings provided
     */
    public double getAnalog(String action) {
        if (bindings == null) {
            DriverStation.reportError("Button bindings not configured yet!", false);
            return 0.0;
        }

        String trigger = bindings.getProperty(action, "None");

        if (trigger == "None") {
            return 0.0;
        }

        int triggerID;
        if (joyType == 0) {
            switch (trigger) {
                case "LeftX":
                    triggerID = 0;
                    break;
                
                case "LeftY":
                    triggerID = 1;
                    break;

                case "RightX":
                    triggerID = 4;
                    break;

                case "RightY":
                    triggerID = 5;
                    break;

                case "LeftTrigger":
                    triggerID = 2;
                    break;

                case "RightTrigger":
                    triggerID = 3;
                    break;

                default:
                    DriverStation.reportError("There is no analog trigger with the name " + trigger, false);
                    return 0.0;
            }
        } else if (joyType == 1) {
            switch (trigger) {
                case "X":
                    triggerID = 0;
                    break;

                case "Y":
                    triggerID = 1;
                    break;

                case "Rotate":
                    triggerID = 2;
                    break;

                case "Slider":
                    triggerID = 3;
                    break;
            
                default:
                    DriverStation.reportError("There is no analog trigger with the name " + trigger, false);
                    return 0.0;
            }
        } else {
            DriverStation.reportError("There is no joystick type of " + joyType, false);
            return 0.0;
        }

        return controller.getRawAxis(triggerID);
    }

    public JoystickButton getWPIJoystickButton(String action) {
        if (bindings == null) {
            DriverStation.reportError("Button bindings not configured yet!", false);
            return new JoystickButton(controller, 100);
        }

        String button = bindings.getProperty(action);

        int buttonID;
        if (joyType == 0) {
            switch (button) {
                case "X":
                    buttonID = 3;
                    break;
                
                case "Y":
                    buttonID = 4;
                    break;

                case "A":
                    buttonID = 1;
                    break;
                
                case "B":
                    buttonID = 2;
                    break;

                case "LeftBumper":
                    buttonID = 5;
                    break;

                case "RightBumper":
                    buttonID = 6;
                    break;

                case "Start":
                    buttonID = 8;
                    break;

                case "Back":
                    buttonID = 7;
                    break;

                case "LeftJoystick":
                    buttonID = 9;
                    break;

                case "RightJoystick":
                    buttonID = 10;
                    break;
            
                default:
                    DriverStation.reportError("There is no button with the name " + button, false);
                    return new JoystickButton(controller, 100);
            }
        } else if (joyType == 1) {
            buttonID = Integer.parseInt(button);
        } else {
            DriverStation.reportError("There is no joystick type of " + joyType, false);
            return new JoystickButton(controller, 100);
        }

        return new JoystickButton(controller, buttonID);
    }

    public Trigger getDPadTrigger(String action) {
        return new Trigger(() -> this.getDPad(action));
    }

    public boolean getDPad(String action) {
        if (bindings == null) {
            DriverStation.reportError("Button bindings not configured yet!", false);
            return false;
        }

        String button = bindings.getProperty(action, "None");

        if (button.equals("None")) {
            return false;
        }

        int direction;
        switch (button) {
            case "Up":
                direction = 0;
                break;
            
            case "Right":
                direction = 90;
                break;

            case "Down":
                direction = 180;
                break;
            
            case "Left":
                direction = 270;
                break;
        
            default:
                DriverStation.reportError("There is no direction with the name " + button, false);
                return false;
        }

        return direction == controller.getPOV();
    }

    /**
     * Set the intensity of the joystick rumble
     * 
     * @param rumble The intensity of the rumble (0 to 1, 0 being off and 1 being max)
     */
    public void setRumble(double rumble) {
        controller.setRumble(RumbleType.kLeftRumble, rumble);
        controller.setRumble(RumbleType.kRightRumble, rumble);
    }

    /**
     * Configure the button bindings using a Properties object
     * 
     * @param bindings The Properties object to read in the button bindings from
     */
    public void configureBindings(Properties bindings) {
        this.bindings = bindings;
    }
}
