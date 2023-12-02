package commands.admin;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioFiles.AudioFile;
import entities.audioFiles.Song;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Artist;
import entities.users.Host;
import entities.users.User;
import entities.users.functionalities.UserPlayer;
import gateways.PlayerAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
            output.setMessage(input.getUsername() + " was successfully deleted.");
            return;
        }

        List<User> onlineUsers = MyDatabase.getInstance().getUsers()
                .stream().filter(User::isOnline)
                .filter(user -> user.getPlayer() != null
                        && !user.getPlayer().getTypeLoaded().isEmpty()).toList();
        System.out.println(onlineUsers.stream().map(AbstractUser::getUsername).toList());
        for (User user : onlineUsers) {
            UserPlayer player = user.getPlayer();
            if (player != null && !player.getTypeLoaded().isEmpty()) {
                player.updatePlayer(input.getTimestamp());
            }
        }

        List<String> listeningTo = onlineUsers.stream().map(User::getPlayer).filter(Objects::nonNull)
                .map(UserPlayer::getTypeLoaded)
                .filter(typeLoaded -> !typeLoaded.isEmpty()).toList();

//                .map(UserPlayer::getCurrentPlayed).filter(Objects::nonNull).map(AudioFile::getCreator)
//                .filter(string -> string.equals(input.getUsername())).toList();
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
