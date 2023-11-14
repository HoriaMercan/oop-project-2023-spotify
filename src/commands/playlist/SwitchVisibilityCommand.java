package commands.playlist;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.User;
import entities.audioCollections.Playlist;

public class SwitchVisibilityCommand extends AbstractCommand {
	public SwitchVisibilityCommand(SwitchVisibilityInput switchVisibilityInput) {
		super(switchVisibilityInput);
		this.commandOutput = new SwitchVisibilityOutput(switchVisibilityInput);
	}

	public static class SwitchVisibilityInput extends AbstractCommand.CommandInput {
		public Integer getPlaylistId() {
			return playlistId;
		}

		public void setPlaylistId(Integer playlistId) {
			this.playlistId = playlistId;
		}

		public Integer playlistId;
		@Override
		public AbstractCommand getCommandFromInput() {
			return new SwitchVisibilityCommand(this);
		}
	}

	public static class SwitchVisibilityOutput extends AbstractCommand.CommandOutput {
		public SwitchVisibilityOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}

	@Override
	public SwitchVisibilityOutput getCommandOutput() {
		return (SwitchVisibilityOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		SwitchVisibilityInput input = (SwitchVisibilityInput) this.commandInput;
		SwitchVisibilityOutput output = (SwitchVisibilityOutput) this.commandOutput;

		User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
//		user.getPlayer().updatePlayer(input.getTimestamp());
		if (user.isPlaylistIDInUserList(input.getPlaylistId())) {
			Playlist playlist = MyDatabase.getInstance().
					findPlaylistByName(user.getPlaylistFromID(input.getPlaylistId()));

			playlist.setPublic(!playlist.isPublic());
			String visibilityType;
			if (playlist.isPublic())
				visibilityType = "public";
			else
				visibilityType = "private";
			output.setMessage("Visibility status updated successfully to " +
					 visibilityType + ".");
			return;
		}
		output.setMessage("The specified playlist ID is too high.");
	}
}
