package commands.statistics;

import commands.AbstractCommand;

import java.util.List;

public class GetTop5SongsCommand extends AbstractCommand {
	public GetTop5SongsCommand(GetTop5SongsInput getTop5SongsInput) {
		super(getTop5SongsInput);
		this.commandOutput = new GetTop5SongsOutput(getTop5SongsInput);
	}

	public static class GetTop5SongsInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new GetTop5SongsCommand(this);
		}
	}

	public static class GetTop5SongsOutput extends AbstractCommand.CommandOutput {
		List<String> result;
		public GetTop5SongsOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
