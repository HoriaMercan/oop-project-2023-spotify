package commands.player;

import commands.AbstractCommand;
import gateways.PlayerAPI;

public class PlayPauseCommand extends AbstractCommand{
	public PlayPauseCommand(PlayPauseInput playPauseInput) {
		super(playPauseInput);
		this.commandOutput = new PlayPauseOutput(playPauseInput);

	}
	public static class PlayPauseInput extends AbstractCommand.CommandInput {
		public PlayPauseInput() {
			super();
		}

		@Override
		public AbstractCommand getCommandFromInput() {
			return new PlayPauseCommand(this);
		}
	}

	public static class PlayPauseOutput extends AbstractCommand.CommandOutput {
		public PlayPauseOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}

	@Override
	public PlayPauseOutput getCommandOutput() {
		return (PlayPauseOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		PlayPauseInput input = (PlayPauseInput) this.commandInput;
		PlayPauseOutput output = (PlayPauseOutput) this.commandOutput;

		output.setMessage(PlayerAPI.getPlayPauseMessage(
				input.getUsername(), input.getTimestamp()
		));

	}
}
