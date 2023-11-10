package commands.player;

import commands.AbstractCommand;

public class ForwardCommand extends AbstractCommand {
	public ForwardCommand(ForwardInput forwardInput) {
		super(forwardInput);
		this.commandOutput = new ForwardOutput(forwardInput);
	}

	public static class ForwardInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new ForwardCommand(this);
		}
	}

	public static class ForwardOutput extends AbstractCommand.CommandOutput {
		public ForwardOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
