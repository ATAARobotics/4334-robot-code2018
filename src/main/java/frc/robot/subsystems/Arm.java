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
    // top pos claw = 120   low pos claw = 0   mid = 60, wanted mid position = 60
    private PIDController armPID = new PIDController(0.02, 0, 0);
    private AnalogPotentiometer ArmPotentiometer = new AnalogPotentiometer(1, 1000, -25);

    public double setPoint = 50;
    public double PID_P = 0.07;
    public double PID_I = 0.00;
    public double PID_D = 0.01;

    
    public Arm() {
        armMotor = new TalonSRX(Constants.ARM_MOTOR);
        armMotor.setInverted(true);
        SmartDashboard.setDefaultNumber("SetPoint", 10); // from 0 to 100

        // smartdashboard PID
        SmartDashboard.setDefaultNumber("PID-P", 0.07);
        SmartDashboard.setDefaultNumber("PID-I", 0);
        SmartDashboard.setDefaultNumber("PID-D", 0.01);

        armElbow = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.ARM_ELBOW[0], Constants.ARM_ELBOW[1]);
        armElbow.set(Value.kForward);
    }

    public void goToPosition(){
        SmartDashboard.putNumber("SetPoint", setPoint);
        armPID.setSetpoint(setPoint);

    }

    public void checkPosition(ArmDirection pos){
        // up to down
        if (pos == ArmDirection.UP){
            armMotion = ArmDirection.UP;
            setPoint = Constants.HIGH_POS;
            armPID.setSetpoint(setPoint);
        }
        else if (pos == ArmDirection.MID){
            armMotion = ArmDirection.MID;
            setPoint = Constants.MID_POS;
            armPID.setSetpoint(setPoint);
        }
        else {
            armMotion = ArmDirection.DOWN;
            setPoint = Constants.LOW_POS;
            armPID.setSetpoint(setPoint);
        }
    }

    public ArmDirection getArmPos() {
        if (ArmPotentiometer.get() <= 40) {
            return ArmDirection.DOWN;
        } else if (ArmPotentiometer.get() >= 60) {
            return ArmDirection.UP;
        } else {
            return ArmDirection.MID;
        }
    }

    public ArmDirection getArmMotion() {
        return armMotion;
    }

    public void moveArm(double speed){
        // sets and gets speed
        SmartDashboard.putString("ARM MOTION", this.armMotion.toString());

        armMotor.set(ControlMode.PercentOutput, speed);
    }

    public void stopArm() {
        armMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void setElbow(ArmDirection direction) {
        if (direction == ArmDirection.MID) {
            armElbow.set(Value.kReverse);
        } else {
            armElbow.set(Value.kForward);
            armElbow.notify();
        }
    }

    public double getPIDvalue() {
        return armPID.calculate(ArmPotentiometer.get());
    }

    public double getP() {
        return armPID.getP();
    }

    public void changePID() {
        double newP = SmartDashboard.getNumber("PID-P", PID_P);
        double newI = SmartDashboard.getNumber("PID-I", PID_I);
        double newD = SmartDashboard.getNumber("PID-D", PID_D);
        
        armPID.setP(newP);
        armPID.setI(newI);
        armPID.setD(newD);
        // armPID = new PIDController(newP, newI, newD);
    }

    public double getPotentioValue() {
        return ArmPotentiometer.get();
    }

}
