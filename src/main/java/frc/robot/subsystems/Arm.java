package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class Arm extends SubsystemBase {

    public enum ArmDirection {
        DOWN,
        MIDDLE,
        UP
    }

    private TalonSRX armMotor;

    private DoubleSolenoid armElbow;
    private ArmDirection armElbowPos = ArmDirection.DOWN;

    private DoubleSolenoid clawPinch;

    private AnalogInput armPotentiometer;

    public Arm() {
        armMotor = new TalonSRX(RobotMap.ARM_MOTOR);

        armElbow = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.ARM_ELBOW[0], RobotMap.ARM_ELBOW[1]);
        clawPinch = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.CLAW_PINCH[0], RobotMap.CLAW_PINCH[1]);

        armPotentiometer = new AnalogInput(RobotMap.ARM_POTENTIOMETER);

        armElbow.set(Value.kForward);
        clawPinch.set(Value.kReverse);
    }

    public void setElbow(ArmDirection direction) {
        if (armElbowPos != direction) {
            if (direction == ArmDirection.UP) {
                armElbow.set(Value.kReverse);
            } else {
                armElbow.set(Value.kForward);
            }
        }
    }

    public void moveArm(ArmDirection direction) {
        double speed = direction == ArmDirection.UP ? -0.1 : 0.1;

        armMotor.set(ControlMode.PercentOutput, speed);
    }

    public ArmDirection getArmPos() {
        if (armPotentiometer.getAverageVoltage() <= RobotMap.ARM_DOWN_POS) {
            return ArmDirection.DOWN;
        } else if (armPotentiometer.getAverageVoltage() >= RobotMap.ARM_UP_POS) {
            return ArmDirection.UP;
        }
        return ArmDirection.MIDDLE;
    }
}
