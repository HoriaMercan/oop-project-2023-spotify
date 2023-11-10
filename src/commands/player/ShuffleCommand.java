package commands.player;

import commands.AbstractCommand;

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
}
