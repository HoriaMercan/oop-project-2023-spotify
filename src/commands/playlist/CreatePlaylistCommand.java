package commands.playlist;

import commands.AbstractCommand;
import gateways.PlayerAPI;

public class CreatePlaylistCommand extends AbstractCommand {
	public CreatePlaylistCommand(CreatePlaylistInput createPlaylistInput) {
		super(createPlaylistInput);
		this.commandOutput = new CreatePlaylistOutput(createPlaylistInput);
	}

	public static class CreatePlaylistInput extends AbstractCommand.CommandInput {
		public String getPlaylistName() {
			return playlistName;
		}

		public void setPlaylistName(String playlistName) {
			this.playlistName = playlistName;
		}

		protected String playlistName;
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

	@Override
	public CreatePlaylistOutput getCommandOutput() {
		return (CreatePlaylistOutput)this.commandOutput;
	}

	@Override
	public void executeCommand() {
		CreatePlaylistInput input = (CreatePlaylistInput) this.commandInput;
		CreatePlaylistOutput output = (CreatePlaylistOutput) this.commandOutput;

		output.setMessage(PlayerAPI.getCreatePlaylistCommand(
				input.getUsername(), input.getPlaylistName()));
	}
}
