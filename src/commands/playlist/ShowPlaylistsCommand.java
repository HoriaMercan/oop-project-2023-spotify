package commands.playlist;

import commands.AbstractCommand;
import entities.audioCollections.Playlist;

public class ShowPlaylistsCommand extends AbstractCommand {
	public ShowPlaylistsCommand(ShowPlaylistsInput showPlaylistsInput) {
		super(showPlaylistsInput);
		this.commandOutput = new ShowPlaylistsOutput(showPlaylistsInput);
	}

	public static class ShowPlaylistsInput extends AbstractCommand.CommandInput {
		public Integer playlistId;
		@Override
		public AbstractCommand getCommandFromInput() {
			return new ShowPlaylistsCommand(this);
		}
	}

	public static class ShowPlaylistsOutput extends AbstractCommand.CommandOutput {
		Playlist result;
		public ShowPlaylistsOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
