package commands.statistics;

import commands.AbstractCommand;

import java.util.List;

public class ShowPreferredSongsCommand extends AbstractCommand {
	public ShowPreferredSongsCommand(ShowPreferredSongsInput showPreferredSongsInput) {
		super(showPreferredSongsInput);
		this.commandOutput = new ShowPreferredSongsOutput(showPreferredSongsInput);
	}

	public static class ShowPreferredSongsInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new ShowPreferredSongsCommand(this);
		}
	}

	public static class ShowPreferredSongsOutput extends AbstractCommand.CommandOutput {
		List<String> result;
		public ShowPreferredSongsOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
