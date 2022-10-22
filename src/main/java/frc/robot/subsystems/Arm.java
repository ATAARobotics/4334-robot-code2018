package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Arm extends SubsystemBase{

    public enum ArmDirection {
        DOWN,
        MID,
        UP;
    }

    private TalonSRX armMotor;
    private DoubleSolenoid armElbow;
    private ArmDirection armMotion = ArmDirection.DOWN;

    private PIDController armPID = new PIDController(0.02, 0, 0);
    private AnalogPotentiometer ArmPotentiometer = new AnalogPotentiometer(1, 1000, -90);

    public double setPoint = 50;

    
    public Arm() {
        armMotor = new TalonSRX(Constants.ARM_MOTOR);
        armMotor.setInverted(true);
        SmartDashboard.setDefaultNumber("SetPoint", 50); // from 0 to 100

        armElbow = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.ARM_ELBOW[0], Constants.ARM_ELBOW[1]);
        armElbow.set(Value.kForward);
    }

    public void goToPosition(){
        setPoint = SmartDashboard.getNumber("SetPoint", 50);
        armPID.setSetpoint(setPoint);

    }

    public ArmDirection getArmPos() {
        if (ArmPotentiometer.get() <= Constants.ARM_DOWN_POS + Constants.ARM_TOLERANCE / 2) {
            return ArmDirection.DOWN;
        } else if (ArmPotentiometer.get() >= Constants.ARM_UP_POS - Constants.ARM_TOLERANCE) {
            return ArmDirection.UP;
        } else {
            return ArmDirection.MID;
        }
    }

    public ArmDirection getArmMotion() {
        return armMotion;
    }

    public void moveArm(double speed){
        if (speed > 0.1) {
            this.armMotion = ArmDirection.UP;
        } else if (speed < 0.1) {
            this.armMotion = ArmDirection.DOWN;
        }

        SmartDashboard.putString("ARM MOTION", this.armMotion.toString());

        armMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopArm() {
        armMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void setElbow(ArmDirection direction) {
        if (direction == ArmDirection.UP) {
            armElbow.set(Value.kReverse);
        } else {
            armElbow.set(Value.kForward);
        }
    }

    public double getPIDvalue() {
        return armPID.calculate(ArmPotentiometer.get());
    }

    public double getP() {
        return armPID.getP();
    }

    public double getPotentioValue() {
        return ArmPotentiometer.get();
    }

}
