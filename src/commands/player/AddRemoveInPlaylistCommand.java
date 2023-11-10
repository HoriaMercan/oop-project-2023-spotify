package commands.player;

import commands.AbstractCommand;

public class AddRemoveInPlaylistCommand extends AbstractCommand {
	public AddRemoveInPlaylistCommand(AddRemoveInPlaylistInput addRemoveInPlaylistInput) {
		super(addRemoveInPlaylistInput);
		this.commandOutput = new AddRemoveInPlaylistOutput(addRemoveInPlaylistInput);
	}

	public static class AddRemoveInPlaylistInput extends AbstractCommand.CommandInput {
		private Integer playlistId;

		public void setPlaylistId(Integer playlistId) {
			this.playlistId = playlistId;
		}
		public Integer getPlaylistId() {
			return this.playlistId;
		}
		@Override
		public AbstractCommand getCommandFromInput() {
			return new AddRemoveInPlaylistCommand(this);
		}
	}

	public static class AddRemoveInPlaylistOutput extends AbstractCommand.CommandOutput {
		public AddRemoveInPlaylistOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
