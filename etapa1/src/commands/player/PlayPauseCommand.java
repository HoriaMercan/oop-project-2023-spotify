package commands.player;

import commands.AbstractCommand;
import gateways.PlayerAPI;

public final class PlayPauseCommand extends AbstractCommand {
    public PlayPauseCommand(final PlayPauseInput playPauseInput) {
        super(playPauseInput);
        this.commandOutput = new PlayPauseOutput(playPauseInput);

    }

    @Override
    public void executeCommand() {
        PlayPauseInput input = (PlayPauseInput) this.commandInput;
        PlayPauseOutput output = (PlayPauseOutput) this.commandOutput;

        output.setMessage(PlayerAPI.getPlayPauseMessage(
                input.getUsername(), input.getTimestamp()
        ));

    }

    @Override
    public PlayPauseOutput getCommandOutput() {
        return (PlayPauseOutput) this.commandOutput;
    }

    public static final class PlayPauseInput extends AbstractCommand.CommandInput {
        public PlayPauseInput() {
            super();
        }

        @Override
        public AbstractCommand getCommandFromInput() {
            return new PlayPauseCommand(this);
        }
    }

    public static final class PlayPauseOutput extends AbstractCommand.CommandOutput {
        public PlayPauseOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
