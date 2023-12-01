package commands.player;

import commands.AbstractCommand;
import entities.requirements.RequireOnline;
import gateways.PlayerAPI;

public final class LoadCommand extends AbstractCommand implements RequireOnline {
    public LoadCommand(final LoadInput loadInput) {
        super(loadInput);
        this.commandOutput = new LoadOutput(loadInput);
    }

    @Override
    public void executeCommand() {
        LoadInput input = (LoadInput) commandInput;
        LoadOutput output = (LoadOutput) commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

        output.setMessage(PlayerAPI.getLoadMessage(input.getUsername(), input.getTimestamp()));
    }

    public LoadOutput getCommandOutput() {
        return (LoadOutput) this.commandOutput;
    }

    public static final class LoadInput extends AbstractCommand.CommandInput {
        public LoadInput() {
            super();
        }

        @Override
        public AbstractCommand getCommandFromInput() {
            return new LoadCommand(this);
        }
    }

    public static final class LoadOutput extends AbstractCommand.CommandOutput {
        public LoadOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
