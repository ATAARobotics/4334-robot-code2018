package ca.fourthreethreefour;

import edu.first.module.Module;
import edu.first.module.subsystems.Subsystem;
import edu.first.robot.IterativeRobotAdapter;

public class Robot extends IterativeRobotAdapter {
	
	//Creates Subsystems AUTO and TELEOP that contains the code for whatever in the {}'s
	private final Subsystem AUTO_MODULES = new Subsystem(
			new Module[] { arm, drive });
	
	private final Subsystem TELEOP_MODULES = new Subsystem(
			new Module[] { arm, drive });
	
	//Puts the above two subsystems into this subsystem. Subsystemception
	private final Subsystem ALL_MODULES =  new Subsystem(new Module[] { AUTO_MODULES, TELEOP_MODULES });
	
	/*	Constructor for the custom Robot class. Needed because IterativeRobotAdapter 
		needs a name for some reason. */
	public Robot() {
		super("ATA 2018");
	}
	
	@Override
	public void init() {
		ALL_MODULES.init();
	}
	
	@Override
	public void periodicDisabled() {
		//TODO Robot should check settings file here
	}
	
	@Override
	public void initAutonomous() {
		AUTO_MODULES.enable();
	}
	
	@Override
	public void initTeleoperated() {
		TELEOP_MODULES.enable();
	}
	
	@Override
	public void periodicTeleoperated() {
		
	}
}
