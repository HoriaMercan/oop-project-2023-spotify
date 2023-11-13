package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.User;
import gateways.PlayerAPI;

public class RepeatCommand extends AbstractCommand {
	public RepeatCommand(RepeatInput repeatInput) {
		super(repeatInput);
		this.commandOutput = new RepeatOutput(repeatInput);
	}

	public static class RepeatInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new RepeatCommand(this);
		}
	}

	public static class RepeatOutput extends AbstractCommand.CommandOutput {
		public RepeatOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}

	@Override
	public RepeatOutput getCommandOutput() {
		return (RepeatOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		RepeatInput input = (RepeatInput) this.commandInput;
		RepeatOutput output = (RepeatOutput) this.commandOutput;

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
}
