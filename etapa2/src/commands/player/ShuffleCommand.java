package commands.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.User;
import entities.requirements.RequireOnline;

public final class ShuffleCommand extends AbstractCommand implements RequireOnline {
    public ShuffleCommand(final ShuffleInput shuffleInput) {
        super(shuffleInput);
        this.commandOutput = new ShuffleOutput(shuffleInput);
    }

    @Override
    public void executeCommand() {
        ShuffleInput input = (ShuffleInput) this.commandInput;
        ShuffleOutput output = (ShuffleOutput) this.commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        user.getPlayer().updatePlayer(input.getTimestamp());
        if (user.getPlayer().getTypeLoaded().isEmpty()) {
            output.setMessage("Please load a source before using the shuffle function.");
            return;
        }

        if (!user.getPlayer().getTypeLoaded().equals("playlist") &&
        !user.getPlayer().getTypeLoaded().equals("album")) {
            output.setMessage("The loaded source is not a playlist or an album.");
            return;
        }

        if (user.getPlayer().isShuffle()) {
            user.getPlayer().undoShuffle();
            output.setMessage("Shuffle function deactivated successfully.");
            return;
        }

        user.getPlayer().doShuffle(input.getSeed());
        output.setMessage("Shuffle function activated successfully.");
    }

    @Override
    public ShuffleOutput getCommandOutput() {
        return (ShuffleOutput) commandOutput;
    }

    public static final class ShuffleInput extends AbstractCommand.CommandInput {
        @JsonIgnoreProperties(ignoreUnknown = true)
        private Integer seed = 0;

        public Integer getSeed() {
            return seed;
        }

        public void setSeed(final Integer seed) {
            this.seed = seed;
        }

        @Override
        public AbstractCommand getCommandFromInput() {
            return new ShuffleCommand(this);
        }
    }

    public static final class ShuffleOutput extends AbstractCommand.CommandOutput {
        public ShuffleOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
