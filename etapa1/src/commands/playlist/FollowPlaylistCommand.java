package commands.playlist;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.User;
import entities.audioCollections.Playlist;

public final class FollowPlaylistCommand extends AbstractCommand {
    public FollowPlaylistCommand(final FollowPlaylistInput followPlaylistInput) {
        super(followPlaylistInput);
        this.commandOutput = new FollowPlaylistOutput(followPlaylistInput);
    }

    @Override
    public void executeCommand() {
        FollowPlaylistInput input = (FollowPlaylistInput) this.commandInput;
        FollowPlaylistOutput output = (FollowPlaylistOutput) this.commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        if (user.getPlayer().getLastSelected().isEmpty()) {
            output.setMessage("Please select a source before following or unfollowing.");
            return;
        }

        if (!user.getPlayer().getTypeSearched().equals("playlist")) {
            output.setMessage("The selected source is not a playlist.");
            return;
        }

        Playlist playlist = MyDatabase.getInstance()
                .findPlaylistByName(user.getPlayer().getLastSelected());

        if (user.isPlaylistInUserList(playlist.getName())) {
            output.setMessage("You cannot follow or unfollow your own playlist.");
            return;
        }

        if (playlist.getUnfollowed(user.getUsername())) {
            output.setMessage("Playlist unfollowed successfully.");
            return;
        }

        playlist.getFollowedBy(user.getUsername());
        output.setMessage("Playlist followed successfully.");

    }

    @Override
    public FollowPlaylistOutput getCommandOutput() {
        return (FollowPlaylistOutput) this.commandOutput;
    }

    public static final class FollowPlaylistInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new FollowPlaylistCommand(this);
        }
    }

    public static final class FollowPlaylistOutput extends AbstractCommand.CommandOutput {
        public FollowPlaylistOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
