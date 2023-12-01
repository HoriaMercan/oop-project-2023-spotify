package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.User;
import entities.requirements.RequireOnline;

public final class PrevCommand extends AbstractCommand implements RequireOnline {
    public PrevCommand(final PrevInput prevInput) {
        super(prevInput);
        this.commandOutput = new PrevOutput(prevInput);
    }

    @Override
    public void executeCommand() {
        PrevInput input = (PrevInput) this.commandInput;
        PrevOutput output = (PrevOutput) this.commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        user.getPlayer().updatePlayer(input.getTimestamp());

        if (user.getPlayer().getTypeLoaded().isEmpty()) {
            output.setMessage("Please load a source before returning to the previous track.");
            return;
        }

        user.getPlayer().runPrev();
        output.setMessage("Returned to previous track successfully. The current track is "
                + user.getPlayer().getCurrentPlayedName() + ".");
    }

    @Override
    public PrevOutput getCommandOutput() {
        return (PrevOutput) this.commandOutput;
    }

    public static final class PrevInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new PrevCommand(this);
        }
    }

    public static final class PrevOutput extends AbstractCommand.CommandOutput {
        public PrevOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
