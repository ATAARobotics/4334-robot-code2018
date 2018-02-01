/**
 * This code should provide an autonomous which will cross the auto line (10 feet) to alliance members who lack an auto.
 * Written assuming it is for the kit bot with 4 CIMs total (2 per side) and SPARK speed controllers. 
 * We cannot use the ATA library for this project. We can use the WPI library though.
 * Most code is currently copied from the Tank Drive code for the test control system.
 */
package ca.fourthreethreefour.rookie.auto;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class rookieAuto extends IterativeRobot {
	
	Spark frontLeft = new Spark(0),
			backLeft = new Spark(1),
			frontRight = new Spark(2),
			backRight = new Spark(3);
	
	@SuppressWarnings("deprecation")
	RobotDrive drivetrain = new RobotDrive(0,1,2,3);

	/**
	 * Inverted left side motors so that the robot will drive in one direction when told to move forward.
	 * Not currently sure if that direction is forward or backward. 
	 */
	@Override
	public void robotInit() {
		frontLeft.setInverted(true);
		backLeft.setInverted(true);
	
	
	}
}
		
