package commands.player;

import commands.AbstractCommand;
import entities.requirements.RequireOnline;
import gateways.PlayerAPI;

public final class LikeCommand extends AbstractCommand implements RequireOnline {
    public LikeCommand(final LikeInput likeInput) {
        super(likeInput);
        this.commandOutput = new LikeOutput(likeInput);
    }

    @Override
    public void executeCommand() {
        LikeInput input = (LikeInput) this.commandInput;
        LikeOutput output = (LikeOutput) this.commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

        output.setMessage(
                PlayerAPI.getLikeMessage(input.getUsername(), input.getTimestamp())
        );
    }

    @Override
    public LikeOutput getCommandOutput() {
        return (LikeOutput) this.commandOutput;
    }

    public static final class LikeInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new LikeCommand(this);
        }
    }

    public static final class LikeOutput extends AbstractCommand.CommandOutput {
        public LikeOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
