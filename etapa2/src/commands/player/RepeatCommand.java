package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.User;
import entities.requirements.RequireOnline;
import gateways.PlayerAPI;

public final class RepeatCommand extends AbstractCommand implements RequireOnline {
    public RepeatCommand(final RepeatInput repeatInput) {
        super(repeatInput);
        this.commandOutput = new RepeatOutput(repeatInput);
    }

    @Override
    public void executeCommand() {
        RepeatInput input = (RepeatInput) this.commandInput;
        RepeatOutput output = (RepeatOutput) this.commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        if (user.getPlayer().getTypeLoaded().isEmpty()) {
            output.setMessage("Please load a source before setting the repeat status.");
            return;
        }

        boolean paused = user.getPlayer().isPaused();

        if (!paused) {
            PlayerAPI.getPlayPauseMessage(input.getUsername(), input.getTimestamp());
        }
        String message = user.getPlayer().changeRepeatStatus();
        output.setMessage("Repeat mode changed to " + message + ".");
        if (!paused) {
            PlayerAPI.getPlayPauseMessage(input.getUsername(), input.getTimestamp());
        }
    }

    @Override
    public RepeatOutput getCommandOutput() {
        return (RepeatOutput) this.commandOutput;
    }

    public static final class RepeatInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new RepeatCommand(this);
        }
    }

    public static final class RepeatOutput extends AbstractCommand.CommandOutput {
        public RepeatOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
