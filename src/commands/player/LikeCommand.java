package commands.player;

import commands.AbstractCommand;

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
}
