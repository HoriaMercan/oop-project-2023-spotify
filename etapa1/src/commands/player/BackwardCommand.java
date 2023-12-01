package commands.player;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.User;

public final class BackwardCommand extends AbstractCommand {
    public BackwardCommand(final BackwardInput backwardInput) {
        super(backwardInput);
        this.commandOutput = new BackwardOutput(backwardInput);
    }

    @Override
    public void executeCommand() {
        BackwardInput input = (BackwardInput) this.commandInput;
        BackwardOutput output = (BackwardOutput) this.commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        user.getPlayer().updatePlayer(input.getTimestamp());

        if (user.getPlayer().getTypeLoaded().isEmpty()) {
            output.setMessage("Please select a source before rewinding.");
            return;
        }

        if (!user.getPlayer().getTypeLoaded().equals("podcast")) {
            output.setMessage("The loaded source is not a podcast.");
            return;
        }

        user.getPlayer().runBackward();
        output.setMessage("Rewound successfully.");
    }

    @Override
    public BackwardOutput getCommandOutput() {
        return (BackwardOutput) this.commandOutput;
    }

    public static final class BackwardInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new BackwardCommand(this);
        }
    }

    public static final class BackwardOutput extends AbstractCommand.CommandOutput {
        public BackwardOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
