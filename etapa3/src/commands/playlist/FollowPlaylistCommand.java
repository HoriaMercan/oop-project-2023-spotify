package commands.playlist;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Notification;
import entities.users.User;
import entities.audioCollections.Playlist;
import entities.requirements.RequireOnline;

public final class FollowPlaylistCommand extends AbstractCommand implements RequireOnline {
    public FollowPlaylistCommand(final FollowPlaylistInput followPlaylistInput) {
        super(followPlaylistInput);
        this.commandOutput = new FollowPlaylistOutput(followPlaylistInput);
    }

    @Override
    public void executeCommand() {
        FollowPlaylistInput input = (FollowPlaylistInput) this.commandInput;
        FollowPlaylistOutput output = (FollowPlaylistOutput) this.commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

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
            user.getFollowedPlaylists().remove(playlist.getName());
            return;
        }

        User creator = MyDatabase.getInstance().findUserByUsername(playlist.getOwner());
        assert creator != null;

        creator.getNotificationsHandler().addNotification(
                new Notification("New follower", "%s followed your playlist"
                        .formatted(user.getUsername())));
        playlist.getFollowedBy(user.getUsername());
        user.getFollowedPlaylists().add(playlist.getName());
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
