package commands.playlist;

import commands.AbstractCommand;
import entities.requirements.RequireOnline;
import gateways.PlayerAPI;

public final class CreatePlaylistCommand extends AbstractCommand implements RequireOnline {
    public CreatePlaylistCommand(final CreatePlaylistInput createPlaylistInput) {
        super(createPlaylistInput);
        this.commandOutput = new CreatePlaylistOutput(createPlaylistInput);
    }

    @Override
    public void executeCommand() {
        CreatePlaylistInput input = (CreatePlaylistInput) this.commandInput;
        CreatePlaylistOutput output = (CreatePlaylistOutput) this.commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

        output.setMessage(PlayerAPI.getCreatePlaylistCommand(
                input.getUsername(), input.getPlaylistName()));
    }

    @Override
    public CreatePlaylistOutput getCommandOutput() {
        return (CreatePlaylistOutput) this.commandOutput;
    }

    public static final class CreatePlaylistInput extends AbstractCommand.CommandInput {
        private String playlistName;

        public String getPlaylistName() {
            return playlistName;
        }

        public void setPlaylistName(final String playlistName) {
            this.playlistName = playlistName;
        }

        @Override
        public AbstractCommand getCommandFromInput() {
            return new CreatePlaylistCommand(this);
        }
    }

    public static final class CreatePlaylistOutput extends AbstractCommand.CommandOutput {
        public CreatePlaylistOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
