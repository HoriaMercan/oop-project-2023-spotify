package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.User;

public class 	PrevCommand extends AbstractCommand {
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

	@Override
	public PrevOutput getCommandOutput() {return (PrevOutput) this.commandOutput;}

	@Override
	public void executeCommand() {
		PrevInput input = (PrevInput) this.commandInput;
		PrevOutput output = (PrevOutput) this.commandOutput;

		User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
		user.getPlayer().updatePlayer(input.getTimestamp());

		if (user.getPlayer().getTypeLoaded().isEmpty()) {
			output.setMessage("Please load a source before returning to the previous track.");
			return;
		}

		user.getPlayer().runPrev();
		output.setMessage("Returned to previous track successfully. The current track is "+
				user.getPlayer().getCurrentPlayedName() +".");
	}
}
