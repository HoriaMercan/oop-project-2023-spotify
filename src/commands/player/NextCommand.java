package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.User;

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

	@Override
	public NextOutput getCommandOutput() {return (NextOutput) this.commandOutput;}

	@Override
	public void executeCommand() {
		NextInput input = (NextInput) this.commandInput;
		NextOutput output = (NextOutput) this.commandOutput;

		User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
		user.getPlayer().updatePlayer(input.getTimestamp());

		if (user.getPlayer().getTypeLoaded().isEmpty()) {
			output.setMessage("Please load a source before skipping to the next track.");
			return;
		}

		user.getPlayer().runNext();
		output.setMessage("Skipped to next track successfully. The current track is "+
				user.getPlayer().getCurrentPlayedName() +".");
	}
}
