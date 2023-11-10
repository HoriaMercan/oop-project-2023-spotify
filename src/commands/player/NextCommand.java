package commands.player;

import commands.AbstractCommand;

public class NextCommand extends AbstractCommand {
	public NextCommand(NextInput nextInput) {
		super(nextInput);
		this.commandOutput = new NextOutput(nextInput);
	}

	public static class NextInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new NextCommand(this);
		}
	}

	public static class NextOutput extends AbstractCommand.CommandOutput {
		public NextOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
