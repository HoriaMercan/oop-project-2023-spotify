package commands.playlist;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.User;
import entities.audioCollections.Playlist;

public final class SwitchVisibilityCommand extends AbstractCommand {
    public SwitchVisibilityCommand(final SwitchVisibilityInput switchVisibilityInput) {
        super(switchVisibilityInput);
        this.commandOutput = new SwitchVisibilityOutput(switchVisibilityInput);
    }

    @Override
    public void executeCommand() {
        SwitchVisibilityInput input = (SwitchVisibilityInput) this.commandInput;
        SwitchVisibilityOutput output = (SwitchVisibilityOutput) this.commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());

        if (user.isPlaylistIDInUserList(input.getPlaylistId())) {
            Playlist playlist = MyDatabase.getInstance().
                    findPlaylistByName(user.getPlaylistFromID(input.getPlaylistId()));

            playlist.setPublic(!playlist.isPublic());
            String visibilityType;
            if (playlist.isPublic()) {
                visibilityType = "public";
            } else {
                visibilityType = "private";
            }
            output.setMessage("Visibility status updated successfully to "
                    + visibilityType + ".");
            return;
        }
        output.setMessage("The specified playlist ID is too high.");
    }

    @Override
    public SwitchVisibilityOutput getCommandOutput() {
        return (SwitchVisibilityOutput) this.commandOutput;
    }

    public static final class SwitchVisibilityInput extends AbstractCommand.CommandInput {
        private Integer playlistId;

        public Integer getPlaylistId() {
            return playlistId;
        }

        public void setPlaylistId(final Integer playlistId) {
            this.playlistId = playlistId;
        }

        @Override
        public AbstractCommand getCommandFromInput() {
            return new SwitchVisibilityCommand(this);
        }
    }

    public static final class SwitchVisibilityOutput extends AbstractCommand.CommandOutput {
        public SwitchVisibilityOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
