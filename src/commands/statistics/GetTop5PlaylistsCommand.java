package commands.statistics;

import commands.AbstractCommand;

import java.util.List;

public class GetTop5PlaylistsCommand extends AbstractCommand {
	public GetTop5PlaylistsCommand(GetTop5PlaylistsInput getTop5PlaylistsInput) {
		super(getTop5PlaylistsInput);
		this.commandOutput = new GetTop5PlaylistsOutput(getTop5PlaylistsInput);
	}

	public static class GetTop5PlaylistsInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new GetTop5PlaylistsCommand(this);
		}
	}

	public static class GetTop5PlaylistsOutput extends AbstractCommand.CommandOutput {
		List<String> result;
		public GetTop5PlaylistsOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
