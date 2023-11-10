package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import gateways.PlayerAPI;

public class LoadCommand extends AbstractCommand {
	public LoadCommand(LoadInput loadInput) {
		super(loadInput);
		this.commandOutput = new LoadOutput(loadInput);
	}
	public static class LoadInput extends AbstractCommand.CommandInput {
		public LoadInput() {
			super();
		}

		@Override
		public AbstractCommand getCommandFromInput() {
			return new LoadCommand(this);
		}
	}
	public static class LoadOutput extends AbstractCommand.CommandOutput {
		public LoadOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}


	public LoadOutput getCommandOutput() {
		return (LoadOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		LoadInput input = (LoadInput) commandInput;
		LoadOutput output = (LoadOutput) commandOutput;

		output.setMessage(PlayerAPI.getLoadMessage(input.getUsername(), input.getTimestamp()));
	}
}
