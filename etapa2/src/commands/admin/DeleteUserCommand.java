package commands.admin;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioCollections.AudioCollection;
import entities.audioCollections.Playlist;
import entities.audioFiles.AudioFile;
import entities.audioFiles.Song;
import entities.users.*;
import entities.users.AbstractUser.UserType;
import entities.users.functionalities.UserPlayer;
import gateways.AdminAPI;
import gateways.PlayerAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class DeleteUserCommand extends AbstractCommand {
    public DeleteUserCommand(final DeleteUserInput deleteUserInput) {
        super(deleteUserInput);
        this.commandOutput = new DeleteUserOutput(deleteUserInput);
    }

    @Override
    public void executeCommand() {
        DeleteUserInput input = (DeleteUserInput) this.commandInput;
        DeleteUserOutput output = (DeleteUserOutput) this.commandOutput;

        AbstractUser newUser =
                MyDatabase.getInstance().findAbstractUserByUsername(input.getUsername());
        if (newUser == null) {
            output.setMessage("The username " + input.getUsername() + " doesn't exist.");
            return;
        }

        if (newUser.getUserType().equals(UserType.NORMAL)) {
            MyDatabase.getInstance().getUsers().remove((User) newUser);
            List <Playlist> usersPlaylists = MyDatabase.getInstance().getPublicPlaylists().stream()
                    .filter(playlist -> playlist.getOwner().equals(newUser.getUsername())).toList();

            MyDatabase.getInstance().getUsers().forEach(new Consumer<User>() {
                @Override
                public void accept(User user) {
                    user.getFollowedPlaylists().removeAll(
                            usersPlaylists.stream()
                                    .map(AudioCollection::getName).toList()
                    );
                }
            });
            MyDatabase.getInstance().getPublicPlaylists().removeAll(usersPlaylists);


            output.setMessage(input.getUsername() + " was successfully deleted.");
            return;
        }

        List<User> onlineUsers = AdminAPI.getOnlineUsers();
        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());

        List<User> listeningTo = AdminAPI.getUsersListeningToCreator((ContentCreator) newUser);

        System.out.println(listeningTo);
        if (!listeningTo.isEmpty()) {
            output.setMessage(input.getUsername() + " can't be deleted.");
            return;
        }

        switch (newUser.getUserType()) {
            case ARTIST -> {
                Artist artist = (Artist) newUser;
                List<Song> allSongs = new ArrayList<>();
                for (Album album : artist.getAlbums()) {
                    allSongs.addAll(album.getSongs());
                }

                for (Song everySong : allSongs) {
                    List<User> users =
                            everySong.userLikedThisSong().stream()
                                    .map(string -> MyDatabase.getInstance()
                                            .findUserByUsername(string))
                                    .toList();
                    for (User user : users) {
                        user.getLikedSongs().remove(everySong.getName());
                    }
                }
                MyDatabase.getInstance().getSongs().removeAll(allSongs);
                MyDatabase.getInstance().getAlbums().removeAll(artist.getAlbums());
                MyDatabase.getInstance().getArtists().remove(artist);
            }
            case HOST -> {
                Host host = (Host) newUser;
                MyDatabase.getInstance().getPodcasts().removeAll(host.getPodcasts());
                MyDatabase.getInstance().getHosts().remove(host);
            }
        }
        output.setMessage(input.getUsername() + " was successfully deleted.");
    }

    @Override
    public DeleteUserOutput getCommandOutput() {
        return (DeleteUserOutput) this.commandOutput;
    }


    public static final class DeleteUserInput extends AbstractCommand.CommandInput {

        @Override
        public AbstractCommand getCommandFromInput() {
            return new DeleteUserCommand(this);
        }
    }

    public static final class DeleteUserOutput extends AbstractCommand.CommandOutput {
        public DeleteUserOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

