package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    private ArmDirection armMotion = ArmDirection.DOWN;

    private AnalogInput armPotentiometer;

    public Arm() {
        armMotor = new TalonSRX(RobotMap.ARM_MOTOR);
        armMotor.setNeutralMode(NeutralMode.Brake);

        armElbow = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.ARM_ELBOW[0], RobotMap.ARM_ELBOW[1]);

        armPotentiometer = new AnalogInput(RobotMap.ARM_POTENTIOMETER);

        armElbow.set(Value.kForward);
    }

    public void setElbow(ArmDirection direction) {
        if (getArmPos() != direction) {
            if (direction == ArmDirection.UP) {
                armElbow.set(Value.kReverse);
            } else {
                armElbow.set(Value.kForward);
            }
        }
    }

    public void moveArm(ArmDirection direction) {
        if (direction != getArmPos()) {
            double speed = direction == ArmDirection.UP ? -0.8 : 0.4;

            armMotor.set(ControlMode.PercentOutput, speed);
        } else {
            stopArm();
        }

        armMotion = direction;
    }

    public void stopArm() {
        armMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public ArmDirection getArmPos() {
        if (armPotentiometer.getAverageVoltage() <= RobotMap.ARM_DOWN_POS) {
            return ArmDirection.DOWN;
        } else if (armPotentiometer.getAverageVoltage() >= RobotMap.ARM_UP_POS) {
            return ArmDirection.UP;
        } else {
            return ArmDirection.MIDDLE;
        }
    }

    public ArmDirection getArmMotion() {
        return armMotion;
    }

    public void log() {
        SmartDashboard.putNumber("Arm Potentiometer", armPotentiometer.getAverageVoltage());
    }
}
