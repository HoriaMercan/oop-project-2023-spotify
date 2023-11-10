package commands.player;

import commands.AbstractCommand;

public class BackwardCommand extends AbstractCommand {
	public BackwardCommand(BackwardInput backwardInput) {
		super(backwardInput);
		this.commandOutput = new BackwardOutput(backwardInput);
	}

	public static class BackwardInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new BackwardCommand(this);
		}
	}

	public static class BackwardOutput extends AbstractCommand.CommandOutput {
		public BackwardOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
