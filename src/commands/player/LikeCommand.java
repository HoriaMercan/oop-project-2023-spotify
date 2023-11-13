package commands.player;

import commands.AbstractCommand;
import gateways.PlayerAPI;

public class LikeCommand extends AbstractCommand {
	public LikeCommand(LikeInput likeInput) {
		super(likeInput);
		this.commandOutput = new LikeOutput(likeInput);
	}

	public static class LikeInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new LikeCommand(this);
		}
	}

	public static class LikeOutput extends AbstractCommand.CommandOutput {
		public LikeOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}

	@Override
	public LikeOutput getCommandOutput() {
		return (LikeOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		LikeInput input = (LikeInput) this.commandInput;
		LikeOutput output = (LikeOutput) this.commandOutput;

		output.setMessage(
				PlayerAPI.getLikeMessage(input.getUsername(), input.getTimestamp())
		);
	}
}
