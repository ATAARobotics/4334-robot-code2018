package edu.first.commands.common;

import edu.first.command.Command;

/**
 * Command that runs in a loop until a condition returns false.
 *
 * @since June 17 13
 * @author Joel Gallant
 */
public abstract class LoopingCommand implements Command {
    private boolean first = true;

    /**
     * Runs {@link #firstLoop()}, then runs {@link #runLoop()} until
     * {@link #continueLoop()} returns false.
     */
    @Override
    public final void run() {
        while (continueLoop()) {
            if (first) {
                first = false;
                try {
                    /*
                     *  This checks if the firstLoop method has been overwritten by a subclass. 
                     *  If firstLoop has not been overwritten, it will run a default runLoop iteration.
                     */
                    if (this.getClass().getMethod("firstLoop").getDeclaringClass().getTypeName().endsWith("LoopingCommand")) {
                        runLoop();
                    } else {
                        firstLoop();
                    }
                } catch (NoSuchMethodException | SecurityException e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                runLoop();
            }
        }
        
        end();
    }

    public void firstLoop() {}
    public void end() {}

    /**
     * Returns whether the loop should run again.
     *
     * @return if loop should continue
     */
    public abstract boolean continueLoop();

    /**
     * Runs the actual instructions of the command.
     */
    public abstract void runLoop();
}
