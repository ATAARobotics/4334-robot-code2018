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
import ca.fourthreethreefour.commands.debug.Logging;
import ca.fourthreethreefour.settings.AutoFile.AutoFileCommand;
import ca.fourthreethreefour.settings.AutoFile.CommandGroupFactory;
import ca.fourthreethreefour.settings.AutoFile.Entry;
import ca.fourthreethreefour.settings.AutoFile.PrintCommand;
import ca.fourthreethreefour.settings.AutoFile.RuntimeCommand;
import ca.fourthreethreefour.settings.AutoFile.Timeout;
import ca.fourthreethreefour.subsystems.Arm;
import ca.fourthreethreefour.subsystems.Drive;
import edu.first.command.Command;
import edu.first.commands.CommandGroup;
import edu.first.commands.common.LoopingCommand;
import edu.wpi.first.wpilibj.DriverStation;

//TODO Comment all them code please

public class AutoFile extends Robot implements Arm, Drive {
	public static final HashMap<String, RuntimeCommand> COMMANDS = new HashMap<>();

	static {
		COMMANDS.put("print", new PrintCommand());
	}

	
	//Timeout settings
	private static class Timeout {
		private long start = 0;
		private long timeout;
		
		public Timeout(long timeout) {
			this.timeout = timeout;
		}
		
		public boolean started() {
			return start!= 0;
		}
		
		public void start() {
			start = System.currentTimeMillis();
		}
		
		public boolean done() {
			return System.currentTimeMillis() - start > timeout;
		}
	}
	
	//Makes a looping command to be used
	private static abstract class LoopingCommandWithTimeout extends LoopingCommand {
        private Timeout timeout;

        public LoopingCommandWithTimeout(Timeout timeout) {
            this.timeout = timeout;
        }

        @Override
        public boolean continueLoop() {
            // not autonomous anymore
            if (!DriverStation.getInstance().isAutonomous() || !DriverStation.getInstance().isEnabled()) {
                Logging.log("command interrupted");
                return false;
            }
            if (!timeout.started()) {
                timeout.start();
            }
            
            if (timeout.done()) {
                Logging.log("command timed out");
            }
            return !timeout.done();
        }
    }
	
	//Commands section!
	private static class PrintCommand implements RuntimeCommand {
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
	
	/*
	 *  Does a thing from last year this year. Someone comment this
	 */	
	public static class Entry {
        final String key, value;

        public Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private List<Entry> entries = new ArrayList<>();
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

        for (String line : contents.split("\n")) {
            if (line.trim().length() == 0) {
                continue;
            } else if (line.contains("=")) {
                String key = line.substring(0, line.indexOf('=') + 1).trim().toLowerCase();
                String value = line.substring(line.indexOf('=') + 1).trim().toLowerCase();
                variables.put(key, value);
            } else {
                String key = line.substring(0, line.indexOf('(')).trim().toLowerCase();
                String value = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')')).trim().toLowerCase();

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

    public Command toCommand() {
        ArrayList<AutoFileCommand> commands = new ArrayList<>();
        for (Entry e : entries) {
            String value = e.value;
            for (Map.Entry<String, String> variable : variables.entrySet()) {
                if (value.contains(variable.getKey())) {
                    value = value.replace(variable.getKey(), variable.getValue());
                }
            }

            commands.add(new AutoFileCommand(e.key, value));
        }

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

    private static class AutoFileCommand {
        public final boolean concurrent;
        public final String name;
        public final List<String> arguments;

        public AutoFileCommand(String key, String arguments) {
            if (key.startsWith("!")) {
                this.concurrent = true;
                this.name = key.substring(1);
            } else {
                this.concurrent = false;
                this.name = key;
            }

            String inner = arguments;
            if (arguments.contains("(") && arguments.contains(")")) {
                inner = arguments.substring(arguments.indexOf('(') + 1, arguments.indexOf(')'));
            }
            this.arguments = Arrays.asList(inner.split(",")).stream().map(String::trim).collect(Collectors.toList());
        }
    }

	private interface RuntimeCommand {
        public Command getCommand(List<String> args);
    }
	
	private static class CommandGroupFactory extends CommandGroup {
		public CommandGroupFactory() {
			
		}
	}

}
