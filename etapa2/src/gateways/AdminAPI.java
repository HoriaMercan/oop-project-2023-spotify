package gateways;

import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioCollections.AudioCollection;
import entities.audioCollections.Playlist;
import entities.audioFiles.AudioFile;
import entities.audioFiles.Song;
import entities.users.*;
import entities.users.functionalities.UserPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public final class AdminAPI {
    private static final MyDatabase DATABASE = MyDatabase.getInstance();

    /**
     * @param audioFiles List of audio files
     * @return Either there are audio files with the same name in the list
     */
    public static boolean audioFilesRepeated(List<? extends AudioFile> audioFiles) {
        Set<String> nameSet = new HashSet<>(audioFiles.stream().map(AudioFile::getName).toList());

        return nameSet.size() != audioFiles.size();
    }

    public static List<User> getOnlineUsers() {
        return DATABASE.getUsers().stream().filter(User::isOnline).toList();
    }

    public static List<User> getPlayingUsers() {
        return getOnlineUsers().stream().filter(user -> user.getPlayer() != null
                && !user.getPlayer().getTypeLoaded().isEmpty()).toList();
    }

    public static void updateAllOnlineUserPlayers(Integer timestamp) {
        getPlayingUsers().forEach(user -> {
            UserPlayer player = user.getPlayer();
            player.updatePlayer(timestamp);
        });
    }

    public static List<User> getUsersListeningToAudioFile(AudioFile audioFile) {
        return getPlayingUsers().stream().filter(user ->
                user.getPlayer().getCurrentPlayed().getName().equals(audioFile.getName())
        ).toList();
    }

    public static List<User> getUsersListeningToAudioCollection(AudioCollection coll) {
        Set<User> userListeningTo = new HashSet<>();

        coll.getAudioFiles().forEach(
                (Consumer<AudioFile>) audioFile
                        -> userListeningTo.addAll(getUsersListeningToAudioFile(audioFile)));

        return userListeningTo.stream().toList();
    }

    public static List<User> getUsersListeningToCreator(ContentCreator creator) {
        List<AudioFile> files = new ArrayList<>();
        creator.getContent().forEach(
                (Consumer<AudioCollection>) audioCollection ->
                        files.addAll(audioCollection.getAudioFiles()));

        Set<User> usersListeningToCreator = new HashSet<>();
        files.forEach(new Consumer<AudioFile>() {
            @Override
            public void accept(AudioFile audioFile) {
                usersListeningToCreator.addAll(getUsersListeningToAudioFile(audioFile));
            }
        });

        return usersListeningToCreator.stream().toList();
    }

    public static AudioCollection getAudioCollectionWithNameFromCreator(ContentCreator creator,
                                                              String name) {
        List<AudioCollection> ans = creator.getContent().stream()
                .filter(a -> a.getName().equals(name)).map(a -> (AudioCollection)a).toList();

        return ans.isEmpty() ? null : ans.get(0);
    }
    public static void removeAudioCollectionFromCreator(ContentCreator creator,
                                                        AudioCollection coll) {
        creator.getContent().remove(coll);
    }

    public static void removeNormalUser(AbstractUser newUser) {
        MyDatabase.getInstance().getUsers().remove((User) newUser);

        // Remove all playlists created by the user ? i don't get why it should function like
        // that tho'
        List <Playlist> usersPlaylists = MyDatabase.getInstance().getPublicPlaylists().stream()
                .filter(playlist -> playlist.getOwner().equals(newUser.getUsername())).toList();

        MyDatabase.getInstance().getUsers().forEach(user -> user.getFollowedPlaylists().removeAll(
                usersPlaylists.stream()
                        .map(AudioCollection::getName).toList()
        ));
        MyDatabase.getInstance().getPublicPlaylists().removeAll(usersPlaylists);

        // Remove the user from followed playlists
        List <Playlist> followedPlaylists = MyDatabase.getInstance().getPublicPlaylists()
                .stream()
                .filter(playlist -> playlist.isFollowedBy(newUser.getUsername())).toList();

        followedPlaylists.forEach(playlist -> playlist.getUnfollowed(newUser.getUsername()));
    }

    public static void removeArtist(AbstractUser newUser) {
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

    public static void removeHost(AbstractUser user) {
        Host host = (Host) user;
        MyDatabase.getInstance().getPodcasts().removeAll(host.getPodcasts());
        MyDatabase.getInstance().getHosts().remove(host);
    }
}
