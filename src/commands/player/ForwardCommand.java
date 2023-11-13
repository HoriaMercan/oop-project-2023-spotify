package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.User;

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

	@Override
	public ForwardOutput getCommandOutput() {
		return (ForwardOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		ForwardInput input = (ForwardInput) this.commandInput;
		ForwardOutput output = (ForwardOutput) this.commandOutput;

		User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
		user.getPlayer().updatePlayer(input.getTimestamp());

		if (user.getPlayer().getTypeLoaded().isEmpty()) {
			output.setMessage("Please load a source before attempting to forward.");
			return;
		}

		if (!user.getPlayer().getTypeLoaded().equals("podcast")) {
			output.setMessage("The loaded source is not a podcast.");
			return;
		}

		user.getPlayer().runForward();
		output.setMessage("Skipped forward successfully.");

	}
}
