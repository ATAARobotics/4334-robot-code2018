package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase{
    
    private TalonSRX IntakeMotor;
    private TalonSRX IntakeWheelLeft;
    private TalonSRX IntakeWheelRight;

    public Intake() {
        IntakeMotor = new TalonSRX(Constants.INTAKE_ARM);
        IntakeWheelLeft = new TalonSRX(Constants.INTAKE_WHEEL[0]);
        IntakeWheelRight = new TalonSRX(Constants.INTAKE_WHEEL[1]);
    }

    public void runIntakeWheel() {
        IntakeWheelLeft.set(ControlMode.PercentOutput, 0.3);
        IntakeWheelRight.set(ControlMode.PercentOutput, 0.3);
    }
    public void runInvIntakeWheel() {
        IntakeWheelLeft.set(ControlMode.PercentOutput, -0.3);
        IntakeWheelRight.set(ControlMode.PercentOutput, -0.3);
    }
    public void stopIntakeWheel() {
        IntakeWheelLeft.set(ControlMode.PercentOutput, 0.0);
        IntakeWheelRight.set(ControlMode.PercentOutput, 0.0);
    }
}
