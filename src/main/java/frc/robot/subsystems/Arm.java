package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.controller.PIDController;
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

    private PIDController armPID = new PIDController(0.007, 0.0, 0.0);

    private DoubleSolenoid armElbow;
    private ArmDirection armMotion = ArmDirection.DOWN;

    private AnalogInput armPotentiometer;

    public Arm() {
        armMotor = new TalonSRX(RobotMap.ARM_MOTOR);
        armMotor.setInverted(true);
        armMotor.setNeutralMode(NeutralMode.Brake);

        armElbow = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, RobotMap.ARM_ELBOW[0], RobotMap.ARM_ELBOW[1]);

        armPotentiometer = new AnalogInput(RobotMap.ARM_POTENTIOMETER);

        armElbow.set(Value.kForward);

        armPID.setSetpoint(RobotMap.ARM_UP_POS * RobotMap.PID_SCALE_FACTOR);
    }

    public void periodic() {
        if (armMotion == ArmDirection.UP) {
            armMotor.set(ControlMode.PercentOutput,
                    armPID.calculate(armPotentiometer.getAverageVoltage() * RobotMap.PID_SCALE_FACTOR));
        }
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
        if (direction != ArmDirection.UP) {
            if (direction != getArmPos()) {
                armMotor.set(ControlMode.PercentOutput, RobotMap.ARM_DOWN_SPEED);
            } else {
                stopArm();
            }
        }

        armMotion = direction;
    }

    public void stopArm() {
        armMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public ArmDirection getArmPos() {
        if (armPotentiometer.getAverageVoltage() <= RobotMap.ARM_DOWN_POS + RobotMap.ARM_TOLERANCE / 2) {
            return ArmDirection.DOWN;
        } else if (armPotentiometer.getAverageVoltage() >= RobotMap.ARM_UP_POS - RobotMap.ARM_TOLERANCE) {
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
