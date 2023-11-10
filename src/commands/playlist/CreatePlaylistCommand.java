package commands.playlist;

import commands.AbstractCommand;

public class CreatePlaylistCommand extends AbstractCommand {
	public CreatePlaylistCommand(CreatePlaylistInput createPlaylistInput) {
		super(createPlaylistInput);
		this.commandOutput = new CreatePlaylistOutput(createPlaylistInput);
	}

	public static class CreatePlaylistInput extends AbstractCommand.CommandInput {
		public String playlistName;
		@Override
		public AbstractCommand getCommandFromInput() {
			return new CreatePlaylistCommand(this);
		}
	}

	public static class CreatePlaylistOutput extends AbstractCommand.CommandOutput {
		public CreatePlaylistOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
