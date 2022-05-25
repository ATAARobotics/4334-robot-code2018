package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import edu.wpi.first.math.MathUtil;

public class Drivetrain extends SubsystemBase {
    private VictorSPX primaryLeftMotor;
    private VictorSPX primaryRightMotor;
    private VictorSPX secondaryLeftMotor;
    private VictorSPX secondaryRightMotor;

    public Drivetrain() {
        primaryLeftMotor = new VictorSPX(RobotMap.LEFT_DRIVE_MOTORS[0]);
        primaryRightMotor = new VictorSPX(RobotMap.RIGHT_DRIVE_MOTORS[0]);
        secondaryLeftMotor = new VictorSPX(RobotMap.LEFT_DRIVE_MOTORS[1]);
        secondaryRightMotor = new VictorSPX(RobotMap.RIGHT_DRIVE_MOTORS[1]);

        secondaryLeftMotor.follow(primaryLeftMotor);
        secondaryRightMotor.follow(primaryRightMotor);

        primaryLeftMotor.setInverted(true);
    }

    public void setMotion(double speed, double rotation) {
        primaryLeftMotor.set(ControlMode.PercentOutput, MathUtil.clamp(speed + rotation, -1.0, 1.0));
        primaryRightMotor.set(ControlMode.PercentOutput, MathUtil.clamp(speed - rotation, -1.0, 1.0));
    }
}
