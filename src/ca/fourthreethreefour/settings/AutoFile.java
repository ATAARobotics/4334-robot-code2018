package ca.fourthreethreefour.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ca.fourthreethreefour.Robot;
import ca.fourthreethreefour.commands.CommandGroupFactory;
import ca.fourthreethreefour.commands.debug.Logging;
import ca.fourthreethreefour.subsystems.Arm;
import ca.fourthreethreefour.subsystems.DriveSensors;
import ca.fourthreethreefour.subsystems.Drive;
import edu.first.command.Command;
import edu.first.commands.CommandGroup;
import edu.first.commands.common.LoopingCommand;
import edu.first.commands.common.SetOutput;
import edu.first.commands.common.WaitCommand;
import edu.first.module.actuators.DualActionSolenoid.Direction;
import edu.wpi.first.wpilibj.DriverStation;

// TODO Fix the spelling and capitalization.

public class AutoFile extends Robot implements Arm, Drive, DriveSensors {

	/**
	 * List of all commands.
	 */
	public static final HashMap<String, RuntimeCommand> COMMANDS = new HashMap<>();

	/*
	 * Commands are registered in this section. To register a command, add it to the
	 * COMMANDS hashmap, with the key being the string in the associated .txt file
	 * that is associated with the command and the value being an instance of the
	 * command you wish to run when that string is found.
	 * 
	 */
	static {
		COMMANDS.put("print", new Print());
		COMMANDS.put("blindDrive", new BlindDrive());
		COMMANDS.put("driveDistance", new DriveDistance());
		COMMANDS.put("driveStraight", new DriveStraight());
		COMMANDS.put("turn", new Turn());
		COMMANDS.put("stop", new Stop());
		COMMANDS.put("wait", new Wait());
		COMMANDS.put("waitUntil", new WaitUntil());
		COMMANDS.put("setGear", new SetGear());
		COMMANDS.put("setArm", new SetArm());
		COMMANDS.put("turnArm", new TurnArm());
	}

	/**
	 * A timer. Determines how much time has passed between the beginning of the
	 * timeout and the current time. The timeout is how long the timer should run
	 * for.
	 * 
	 * @author Trevor Tsang, or maybe Joel
	 * @since 2017, lol
	 */
	private static class Timeout {
		private long start = 0;
		private long timeout; // how long the timer should run for

		public Timeout(long timeout) {
			this.timeout = timeout;
		}

		public boolean started() {
			return start != 0;
		}

		public void start() {
			// The time the timer starts at. Runs on Unix time.
			start = System.currentTimeMillis();
		}

		public boolean done() {
			// If the time passed since the start of the timer is longer than the timeout
			// value, return false
			return System.currentTimeMillis() - start > timeout;
		}
	}

	/**
	 * Constructor for a looping command using {@link Timeout} above to determine
	 * how long the command should run for.
	 * 
	 * @author Trevor, but maybe Joel actually
	 * @since 2017, lol
	 */
	private static abstract class LoopingCommandWithTimeout extends LoopingCommand {
		private Timeout timeout;

		public LoopingCommandWithTimeout(Timeout timeout) {
			this.timeout = timeout;
		}

		@Override
		public boolean continueLoop() {
			// If the driver station isn't in autonomous, or is disabled, stop the command
			// from looping
			if (!DriverStation.getInstance().isAutonomous() || !DriverStation.getInstance().isEnabled()) {
				Logging.log("command interrupted");
				return false;
			}
			if (!timeout.started()) {
				timeout.start();
			}

			if (timeout.done()) {
				Logging.log("Command timed out");
			}
			return !timeout.done();
		}
	}

	/*
	 * Commands section! Add new commands here. To add new commands, create a new
	 * anonymous class that implements RuntimeCommand, and make getCommand() return
	 * a Command with your functionality in run(). PrintCommand is here as an
	 * example.
	 */

	/**
	 * Prints to the console, with the argument being the string to print.
	 * 
	 * @author Trevor or Joel, I forget
	 * @since 2017, lol
	 */
	private static class Print implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			return new Command() {
				@Override
				public void run() {
					System.out.println(args.toString());
				}
			};
		}
	}

	/**
	 * Tank drive command without sensor input. Use only if the sensors are broken.
	 * Arguments are (in order): value of left side, value of right side, duration
	 * of command
	 * 
	 * @author Trevor
	 *
	 */
	private static class BlindDrive implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			double left = Double.parseDouble(args.get(0)),
					right = Double.parseDouble(args.get(1));
			long time = Long.parseLong(args.get(2));
			return new LoopingCommandWithTimeout(new Timeout(time)) {
				@Override
				public void runLoop() {
					drivetrain.tankDrive(left, right);
				}

				@Override
				public void end() {
					drivetrain.stopMotor();
				}
			};
		}
	}

	/**
	 * DriveStraight command without sensor input. Easier to use than BlindDrive,
	 * but not as easy as DriveStraight. Use only if the sensors are broken.
	 * Arguments are: int distance, double compensation, int threshold, time.
	 * 
	 * @author Cool, but originally either Trevor or Joel
	 * @since 2017
	 *
	 */
	// Very similar to DriveStraight below. Check that for comments.
	//TODO Replace compensation with individual left and right values
	private static class DriveDistance implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			int distance = Integer.parseInt(args.get(0));
			double compensation = Double.parseDouble(args.get(1));
			final int threshold = args.size() > 2 ? Integer.parseInt(args.get(2)) : 10;
			long time = args.size() > 3 ? Integer.parseInt(args.get(3)) : 8000;

			return new LoopingCommandWithTimeout(new Timeout(time)) {
				int correctIterations = 0;

				@Override
				public boolean continueLoop() {
					if (!super.continueLoop()) {
						return false;
					}

					if (distancePID.isEnabled() && distancePID.onTarget()) {
						correctIterations++;
						Logging.logf("error", distancePID.getError());
						Logging.logf("threshold", distancePID.getTolerance());
					} else {
						correctIterations = 0;
					}

					return correctIterations < threshold;
				}

				@Override
				public void firstLoop() {
					distancePID.setSetpoint(distance);
					distancePID.enable();
					Logging.log("DriveDistance Started");
				}

				@Override
				public void runLoop() {
					try {
						distancePID.wait(20);
					} catch (InterruptedException e) {
					}
					Logging.put("Distance Error", distancePID.getError());
					double output = speedOutput.get();
					drivetrain.set(output + compensation, output - compensation);
				}

				@Override
				public void end() {
					distancePID.disable();
					leftEncoder.reset();
					rightEncoder.reset();
					Logging.log("DriveDistance Ended");
				}
			};
		}
	}

	/**
	 * Arcade drive designed for going straight. Arguments are: value of int
	 * distance, int of how long to wait to see if in correct spot, long value for
	 * timeout, and double for speed.
	 * 
	 * @author Cool, but originally either Trevor or Joel
	 * @since 2017
	 *
	 */

	private static class DriveStraight implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			int distance = Integer.parseInt(args.get(0)); // Sets distance to the first arg
			final int threshold = args.size() > 1 ? Integer.parseInt(args.get(1)) : 10; // If there is more than one argument (.size) then grabs the value, else by default it's 10
			long time = args.size() > 2 ? Long.parseLong(args.get(2)) : 8000; // Same as above.
			double speed = args.size() > 3 ? Double.parseDouble(args.get(3)) : 1; // Used as a coefficient for speedOutput

			return new LoopingCommandWithTimeout(new Timeout(time)) {
				int correctIterations = 0;

				@Override
				public boolean continueLoop() {

					if (!super.continueLoop()) { // If this continueLoop isn't original continueLoop, return false
						return false;
					}

					if (distancePID.isEnabled() && distancePID.onTarget()) { // If it's enabled and on target
						correctIterations++; // Add one per loop when at the Target.
						Logging.logf("error", distancePID.getError());
						Logging.logf("threshold", distancePID.getTolerance());
					} else { // If it's not on target
						correctIterations = 0; // Set the amount to 0
					}

					return correctIterations < threshold; // If it hasn't reached the threshold, return.
				}

				@Override
				public void firstLoop() {
					leftEncoder.reset(); // Resets the encoders
					rightEncoder.reset();
					distancePID.setSetpoint(distance); // Sets the distance
					distancePID.enable(); // Enables the distance PID

					double angle = navx.getAngle(); // Sets angle to the current angle.
					turnPID.setSetpoint(angle); // Sets the angle
					turnPID.enable(); // Enables the turn PID

					Logging.logf("DriveStraight Setpoint", distancePID.getSetpoint());
				}

				@Override
				public void runLoop() {
					synchronized (distancePID) { // Lets it read any changes done elsewhere
						try {
							distancePID.wait(20); // Waits 20 ticks?
						} catch (InterruptedException e) {
						}
					}

					Logging.put("Distance Error", distancePID.getError());
					drivetrain.arcadeDrive(speedOutput.get() * speed, turningOutput.get());
					// Sets speed and turn to speedOutput and turningOutput. These values are set by the PID controllers.
				}

				@Override
				public void end() {
					Logging.log("DriveStraight Ended");
					distancePID.disable();
					turnPID.disable();
				}
			};
		}
	}

	/**
	 * Arcade drive designed for turning. Arguments are: angle in double, int of how
	 * long to wait to see if in correct spot, Long value for timeout.
	 * 
	 * @author Cool, but originally either Trevor or Joel
	 * @since 2017
	 *
	 */
	// Mainly the same commenting as above DriveStraight
	public static class Turn implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			double angle = Double.parseDouble(args.get(0));
			final int threshold = args.size() > 1 ? Integer.parseInt(args.get(1)) : 10;
			long time = args.size() > 2 ? Long.parseLong(args.get(2)) : 8000;

			return new LoopingCommandWithTimeout(new Timeout(time)) {
				int correctIterations = 0;

				@Override
				public boolean continueLoop() {
					if (!super.continueLoop()) {
						return false;
					}

					if (turnPID.isEnabled() && turnPID.onTarget()) {
						Logging.logf("error", turnPID.getError());
						Logging.logf("threshold", turnPID.getTolerance());
						correctIterations++;
					} else {
						correctIterations = 0;
					}

					return correctIterations < threshold;
				}

				@Override
				public void firstLoop() {
					navx.reset();
					turnPID.setSetpoint(angle);
					turnPID.enable();
					Logging.log("Turn Started");
				}

				@Override
				public void runLoop() {
					synchronized (turnPID) {
						try {
							turnPID.wait(20);
						} catch (InterruptedException e) {} // no? (look at 2017)
					}
					drivetrain.arcadeDrive(0, turningOutput.get()); // Turns it by the turningOutput, but doesn't move.
					Logging.put("Turning Error", turnPID.getError());
				}

				@Override
				public void end() {
					Logging.log("Turn Ended");
					turnPID.disable();
				}
			};
		}
	}

	/**
	 * Stops the bot
	 * 
	 * @author Cool, but originally either Trevor or Joel
	 * @since 2017
	 *
	 */
	private static class Stop implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			return new SetOutput(drivetrain.getDriveStraight(), 0); // Stops by setting drivetrain's drivestraight to 0.
		}
	}

	/**
	 * Waits with set value (double) ticks?
	 * 
	 * @author Cool, but originally either Trevor or Joel
	 * @since 2017
	 *
	 */
	private static class Wait implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {
			if (args.size() != 1) {
				throw new IllegalArgumentException("Error in Wait: Invalid arguments"); // In case no arguments put for
																						// Wait.
			} else {
				return new WaitCommand(Double.parseDouble(args.get(0))); // Runs the WaitCommand with value set.
			}
		}
	}

	/**
	 * Waits until set time (double) of match. Likely best used at beginning of
	 * match or end of match. Beginning to wait for other teams to move out of our
	 * way. End to end autonomous for the start of teleop.
	 * 
	 * @author Cool, but really either Trevor or Joel
	 * @since 2017
	 *
	 */
	private static class WaitUntil implements RuntimeCommand {

		@Override
		public Command getCommand(List<String> args) {
			double time = Double.parseDouble(args.get(0));
			return new Command() {

				@Override
				public void run() {
					// getMatchTime gives the time left in the current period, not how much time has passed.
					// That is why it's 15 - *.getMatchTime(), if the time left was 14, then it would be 1 second gone.
					while (15 - DriverStation.getInstance().getMatchTime() < time) {
						drivetrain.stopMotor();
					}
				}
			};
		}
	}

	/**
	 * Takes arguments for setting the arm.
	 * If 'open', then grabSolenoid will be set to GRAB_OPEN. 
	 * If 'close', then grabSolenoid will be set to GRAB_OPEN.
	 * If 'extend', then armSolenoid will be set to ARM_EXTEND.
	 * If 'retract', then armSolenoid will be set to ARM_RETRACT
	 * 
	 * @author Cool
	 * @since 2018
	 *
	 */
	//TODO Make them actually throw errors to prevent running a command without arguments.
	private static class SetArm implements RuntimeCommand, Arm {
		@Override
		public Command getCommand(List<String> args) {
			String cmd = ((args.get(0)).toLowerCase());
			return new Command() {
				@Override
				public void run() {
					switch (cmd) {
					case "open":
						Arm.grabSolenoid.set(GRAB_OPEN);
						break;
					case "close":
						Arm.grabSolenoid.set(GRAB_CLOSE);
						break;
					case "extend":
						Arm.armSolenoid.set(ARM_EXTEND);
						break;
					case "retract":
						Arm.armSolenoid.set(ARM_RETRACT);
						break;
					case "":
						System.out.println("Error in SetArm: No direction set");
						break;
					default:
						System.out.println("Error in SetArm: Direction set incorrectly");
						break;
					}
				}
			};
		}
	}

	/**
	 * Takes arguments for setting the gear.
	 * If 'low', then gearShifter will be set to LOW_GEAR.
	 * If 'high', then gearShifter will be set to HIGH_GEAR.
	 * 
	 * @author Cool
	 * @since 2018
	 *
	 */

	private static class SetGear implements RuntimeCommand, Drive {
		@Override
		public Command getCommand(List<String> args) {
			String gear = (args.get(0)).toLowerCase(); // Sets gear to the args 0 (arrays start at 0 I do believe) and sets it to lowercase so that no conflicts in capitalization.
			return new Command() {
				@Override
				public void run() {
					switch (gear) {
					case "low":
						Drive.gearShifter.set(LOW_GEAR);
						break;
					case "high":
						Drive.gearShifter.set(HIGH_GEAR);
						break;
					case "":
						System.out.println("Error in SetGear: No gear set");
						Drive.gearShifter.set(Direction.OFF);
						break;
					default:
						System.out.println("Error in SetGear: Gear set incorrectly");
						break;
					}
				}
			};
		}
	}

	/**
	 * Turn the arm with set speed for set time.
	 * First argument is speed that the arm will turn at between -1 and 1.
	 * Second argument is time that it will last for.
	 * 
	 * @author Cool, with reference to last year.
	 * @since 2018
	 *
	 */

	//TODO May be getting a sensor for this motor. Would require another PID controller.
	private static class TurnArm implements RuntimeCommand {
		@Override
		public Command getCommand(List<String> args) {

			Double speed = Double.parseDouble(args.get(0));

			Long time = Long.parseLong(args.get(1));

			return new LoopingCommandWithTimeout(new Timeout(time)) {

				@Override
				public void runLoop() {
					armMotor.setSpeed(speed);
				}

				public void end() {
					armMotor.setSpeed(0);
				}
			};
		}
	}

	// End of commands section

	// TODO Comment this section
	/**
	 * A set of two strings, key and value. Used to store commands.
	 * 
	 */
	public static class Entry {
		final String key, value;

		public Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	/**
	 * List of all objects of type {@link Entry}.
	 */
	private List<Entry> entries = new ArrayList<>();
	/**
	 * List of all commands
	 */
	private Map<String, String> variables = new HashMap<>();

	public AutoFile(File file) throws IOException {
		String contents;
		try (FileInputStream fi = new FileInputStream(file)) {
			StringBuilder builder = new StringBuilder();
			int ch;
			while ((ch = fi.read()) != -1) {
				builder.append((char) ch);
			}
			contents = builder.toString();
		}

		/*
		 * Commands are separated by a new line. This means that every new line in the
		 * .txt file is a separate command.
		 */
		for (String line : contents.split("\n")) {
			if (line.trim().length() == 0) { // ignores blank lines
				continue;
			} else if (line.contains("=")) {
				/*
				 * If the current line contains an '=' sign, everything before the '=' sign is
				 * the key, and everything after it is the value. Used to define values in the
				 * associated .txt file.
				 */
				String key = line.substring(0, line.indexOf('=') + 1).trim().toLowerCase();
				String value = line.substring(line.indexOf('=') + 1).trim().toLowerCase();
				variables.put(key, value);
			} else {
				// everything from the beginning of the line to the first '(' is the key
				String key = line.substring(0, line.indexOf('(')).trim().toLowerCase();
				// everything from after the first '(' to the last ')' is the value
				String value = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')')).trim().toLowerCase();

				// trims unnecessary brackets
				if (value.startsWith("(")) {
					value = value.substring(1);
				}
				if (value.endsWith(")")) {
					value = value.substring(0, value.length() - 1);
				}

				entries.add(new Entry(key, value));
			}
		}
	}

	/**
	 * Converts Strings in the associated .txt into commands, and arranges them to
	 * run in the order they are written in.
	 * 
	 * @return The commands
	 */
	public Command toCommand() {
		// an ArrayList of AutoFileCommands
		ArrayList<AutoFileCommand> commands = new ArrayList<>();
		for (Entry e : entries) { // for each Entry in the list of entries
			String value = e.value;
			for (Map.Entry<String, String> variable : variables.entrySet()) {
				if (value.contains(variable.getKey())) {
					value = value.replace(variable.getKey(), variable.getValue());
				}
			}
			commands.add(new AutoFileCommand(e.key, value));
		}

		// CommandGroupFactory is required because the constructor for CommandGroup is
		// private
		CommandGroup group = new CommandGroupFactory();
		for (AutoFileCommand command : commands) {
			if (COMMANDS.containsKey(command.name)) {
				if (command.concurrent) {
					group.appendConcurrent(COMMANDS.get(command.name).getCommand(command.arguments));
				} else {
					group.appendSequential(COMMANDS.get(command.name).getCommand(command.arguments));
				}
			} else {
				throw new Error(command.name + " not found");
			}
		}
		return group;
	}

	/**
	 * Reads the .txt file and turns lines into sets of commands and arguments.
	 * 
	 * @author Joel, actually
	 * @since 2017, lol
	 *
	 */
	private static class AutoFileCommand {
		public final boolean concurrent;
		public final String name;
		public final List<String> arguments;

		public AutoFileCommand(String key, String arguments) {
			if (key.startsWith("!")) { // if the line starts with '!', make it concurrent
				// concurrent commands run at the same time as the command before them
				this.concurrent = true;
				this.name = key.substring(1);
			} else {
				// if commands are not concurrent, they will run in the order they are written
				this.concurrent = false;
				this.name = key;
			}

			// trims brackets
			String inner = arguments;
			if (arguments.contains("(") && arguments.contains(")")) {
				inner = arguments.substring(arguments.indexOf('(') + 1, arguments.indexOf(')'));
			}
			// arguments are separated by ','
			this.arguments = Arrays.asList(inner.split(",")).stream().map(String::trim).collect(Collectors.toList());
		}
	}

	private interface RuntimeCommand {
		public Command getCommand(List<String> args);
	}

}
