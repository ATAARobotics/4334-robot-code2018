package ca.fourthreethreefour;

import edu.first.module.Module;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;

public class Robot extends IterativeRobotAdapter {

	private final Subsystem TELEOP_MODULES = new Subsystem(
			new Module[] { arm });
	
	//Comment this please, I want to understand this
	public Robot() {
		super("ATA 2018");
	}
	
}
