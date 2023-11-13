package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.User;
import gateways.PlayerAPI;

public class ShuffleCommand extends AbstractCommand {
	public ShuffleCommand(ShuffleInput shuffleInput) {
		super(shuffleInput);
		this.commandOutput = new ShuffleOutput(shuffleInput);
	}

	public static class ShuffleInput extends AbstractCommand.CommandInput {
		public Integer getSeed() {
			return seed;
		}

		public void setSeed(Integer seed) {
			this.seed = seed;
		}

		private Integer seed;
		@Override
		public AbstractCommand getCommandFromInput() {
			return new ShuffleCommand(this);
		}
	}

	public static class ShuffleOutput extends AbstractCommand.CommandOutput {
		public ShuffleOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}

	@Override
	public ShuffleOutput getCommandOutput() {
		return (ShuffleOutput) commandOutput;
	}

	@Override
	public void executeCommand() {
		ShuffleInput input = (ShuffleInput) this.commandInput;
		ShuffleOutput output = (ShuffleOutput) this.commandOutput;

		User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
		user.getPlayer().updatePlayer(input.getTimestamp());
		if (user.getPlayer().getTypeLoaded().isEmpty()) {
			output.setMessage("Please load a source before using the shuffle function.");
			return;
		}

		if (!user.getPlayer().getTypeLoaded().equals("playlist")) {
			output.setMessage("The loaded source is not a playlist.");
			return;
		}

		if (user.getPlayer().isShuffle()) {
			user.getPlayer().undoShuffle();
			output.setMessage("Shuffle function deactivated successfully.");
			return;
		}

		user.getPlayer().doShuffle(input.getSeed());
		output.setMessage("Shuffle function activated successfully.");
	}
}
