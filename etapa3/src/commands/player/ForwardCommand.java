package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.User;
import entities.requirements.RequireOnline;

public final class ForwardCommand extends AbstractCommand implements RequireOnline {
    public ForwardCommand(final ForwardInput forwardInput) {
        super(forwardInput);
        this.commandOutput = new ForwardOutput(forwardInput);
    }

    @Override
    public void executeCommand() {
        ForwardInput input = (ForwardInput) this.commandInput;
        ForwardOutput output = (ForwardOutput) this.commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

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

    @Override
    public ForwardOutput getCommandOutput() {
        return (ForwardOutput) this.commandOutput;
    }

    public static final class ForwardInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new ForwardCommand(this);
        }
    }

    public static final class ForwardOutput extends AbstractCommand.CommandOutput {
        public ForwardOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
