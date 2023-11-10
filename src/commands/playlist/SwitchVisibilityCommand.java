package commands.playlist;

import commands.AbstractCommand;

public class SwitchVisibilityCommand extends AbstractCommand {
	public SwitchVisibilityCommand(SwitchVisibilityInput switchVisibilityInput) {
		super(switchVisibilityInput);
		this.commandOutput = new SwitchVisibilityOutput(switchVisibilityInput);
	}

	public static class SwitchVisibilityInput extends AbstractCommand.CommandInput {
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
}
