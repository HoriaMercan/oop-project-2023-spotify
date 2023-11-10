package commands.playlist;

import commands.AbstractCommand;

public class FollowPlaylistCommand extends AbstractCommand {
	public FollowPlaylistCommand(FollowPlaylistInput followPlaylistInput) {
		super(followPlaylistInput);
		this.commandOutput = new FollowPlaylistOutput(followPlaylistInput);
	}

	public static class FollowPlaylistInput extends AbstractCommand.CommandInput {
		public Integer playlistId;
		@Override
		public AbstractCommand getCommandFromInput() {
			return new FollowPlaylistCommand(this);
		}
	}

	public static class FollowPlaylistOutput extends AbstractCommand.CommandOutput {
		public FollowPlaylistOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
