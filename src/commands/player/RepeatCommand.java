package commands.player;

import commands.AbstractCommand;

public class RepeatCommand extends AbstractCommand {
	public RepeatCommand(RepeatInput repeatInput) {
		super(repeatInput);
		this.commandOutput = new RepeatOutput(repeatInput);
	}

	public static class RepeatInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new RepeatCommand(this);
		}
	}

	public static class RepeatOutput extends AbstractCommand.CommandOutput {
		public RepeatOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
