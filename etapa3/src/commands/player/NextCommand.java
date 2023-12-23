package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.User;
import entities.requirements.RequireOnline;

public final class NextCommand extends AbstractCommand implements RequireOnline {
    public NextCommand(final NextInput nextInput) {
        super(nextInput);
        this.commandOutput = new NextOutput(nextInput);
    }

    @Override
    public void executeCommand() {
        NextInput input = (NextInput) this.commandInput;
        NextOutput output = (NextOutput) this.commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        user.getPlayer().updatePlayer(input.getTimestamp());

        if (user.getPlayer().getTypeLoaded().isEmpty()) {
            output.setMessage("Please load a source before skipping to the next track.");
            return;
        }

        user.getPlayer().runNext(input.getTimestamp());
        if (user.getPlayer().getTypeLoaded().isEmpty()) {
            output.setMessage("Please load a source before skipping to the next track.");
            return;
        }
        output.setMessage("Skipped to next track successfully. The current track is "
                + user.getPlayer().getCurrentPlayedName() + ".");
    }

    @Override
    public NextOutput getCommandOutput() {
        return (NextOutput) this.commandOutput;
    }

    public static final class NextInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new NextCommand(this);
        }
    }

    public static final class NextOutput extends AbstractCommand.CommandOutput {
        public NextOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
