package commands.player;

import commands.AbstractCommand;

public class PrevCommand extends AbstractCommand {
	public PrevCommand(PrevInput prevInput) {
		super(prevInput);
		this.commandOutput = new PrevOutput(prevInput);
	}

	public static class PrevInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new PrevCommand(this);
		}
	}

	public static class PrevOutput extends AbstractCommand.CommandOutput {
		public PrevOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}
}
