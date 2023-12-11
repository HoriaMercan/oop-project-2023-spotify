package gateways;

import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioCollections.AudioCollection;
import entities.audioCollections.Playlist;
import entities.audioFiles.AudioFile;
import entities.audioFiles.Song;
import entities.users.AbstractUser;
import entities.users.Artist;
import entities.users.ContentCreator;
import entities.users.Host;
import entities.users.User;
import entities.users.functionalities.UserPlayer;
import pagesystem.EnumPages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class AdminAPI {

    private static final MyDatabase DATABASE = MyDatabase.getInstance();

    private AdminAPI() {
    }

    /**
     * @param audioFiles List of audio files
     * @return Either there are audio files with the same name in the list
     */
    public static boolean audioFilesRepeated(final List<? extends AudioFile> audioFiles) {
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

    /**
     * Method that updates all user players to a given timestamp
     *
     * @param timestamp Timestamp
     */
    public static void updateAllOnlineUserPlayers(final Integer timestamp) {
        getPlayingUsers().forEach(user -> {
            UserPlayer player = user.getPlayer();
            player.updatePlayer(timestamp);
        });
    }

    /**
     * This methods returns a list of users that have a given audio file loaded in their player
     *
     * @param audioFile AudioFile object
     * @return list of user objects
     */
    public static List<User> getUsersListeningToAudioFile(final AudioFile audioFile) {
        return getPlayingUsers().stream().filter(user
                -> user.getPlayer().getContext().contains(audioFile)).toList();
    }

    /**
     * @param coll AudioCollection object
     * @return list of users listening to a file that is a component of given collection
     */
    public static List<User> getUsersListeningToAudioCollection(final AudioCollection coll) {
        Set<User> userListeningTo = new HashSet<>();

        coll.getAudioFiles().forEach(
                (Consumer<AudioFile>) audioFile
                        -> userListeningTo.addAll(getUsersListeningToAudioFile(audioFile)));

        return userListeningTo.stream().toList();
    }

    /**
     * @param playlistName name of playlist
     * @return list of users that have the given playlist loaded in player
     */
    public static List<User> getUsersListeningToPlaylist(final String playlistName) {
        return getPlayingUsers().stream().filter(user
                -> user.getPlayer().getListeningToPlaylist().equals(playlistName)).toList();
    }

    /**
     * @param user Owner of playlists
     * @return list of users that listens to any of the playlists' of the parameter user
     */
    public static List<User> getUListeningToUPlaylists(final User user) {
        List<User> users = new ArrayList<>();

        List<Playlist> playlists = MyDatabase.getInstance().getPublicPlaylists()
                .stream().filter(playlist -> playlist.getOwner().equals(user.getUsername()))
                .toList();

        playlists.forEach(playlist
                -> users.addAll(getUsersListeningToPlaylist(playlist.getName())));

        return users;
    }

    /**
     * @param creator Creator object (Host/Artist)
     * @return list of users that listen to a creator's file
     */
    public static List<User> getUsersListeningToCreator(final ContentCreator creator) {
        List<AudioFile> files = new ArrayList<>();
        creator.getContent().forEach(
                (Consumer<AudioCollection>) audioCollection ->
                        files.addAll(audioCollection.getAudioFiles()));

        Set<User> usersListeningToCreator = new HashSet<>();
        files.forEach(audioFile
                -> usersListeningToCreator.addAll(getUsersListeningToAudioFile(audioFile)));

        return usersListeningToCreator.stream().toList();
    }

    /**
     * @param creator Creator object
     * @param name
     * @return Audio collection that matches both creator and name
     */
    public static AudioCollection getAudioCollectionWithNameFromCreator(final ContentCreator
                                                                                creator,
                                                                        final String name) {
        List<AudioCollection> ans = creator.getContent().stream()
                .filter(a -> a.getName().equals(name)).map(a -> (AudioCollection) a).toList();

        return ans.isEmpty() ? null : ans.get(0);
    }

    /**
     * @param creator
     * @param coll
     */
    public static void removeAudioCollectionFromCreator(final ContentCreator creator,
                                                        final AudioCollection coll) {
        creator.getContent().remove(coll);
    }

    /**
     * @param abstractUser
     */
    public static void removeNormalUser(final AbstractUser abstractUser) {
        MyDatabase.getInstance().getUsers().remove((User) abstractUser);

        // Remove all playlists created by the user ? i don't get why it should function like
        // that tho'
        List<Playlist> usersPlaylists = MyDatabase.getInstance().getPublicPlaylists().stream()
                .filter(playlist -> playlist.getOwner()
                        .equals(abstractUser.getUsername())).toList();

        MyDatabase.getInstance().getUsers().forEach(user -> user.getFollowedPlaylists().removeAll(
                usersPlaylists.stream()
                        .map(AudioCollection::getName).toList()
        ));
        MyDatabase.getInstance().getPublicPlaylists().removeAll(usersPlaylists);

        // Remove the user from followed playlists
        List<Playlist> followedPlaylists = MyDatabase.getInstance().getPublicPlaylists()
                .stream()
                .filter(playlist -> playlist.isFollowedBy(abstractUser.getUsername())).toList();

        followedPlaylists.forEach(playlist -> playlist.getUnfollowed(abstractUser.getUsername()));
    }

    /**
     * @param abstractUser
     */
    public static void removeArtist(final AbstractUser abstractUser) {
        Artist artist = (Artist) abstractUser;
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
                user.getLikedSongs().remove(everySong);
            }
        }
        MyDatabase.getInstance().getSongs().removeAll(allSongs);
        MyDatabase.getInstance().getAlbums().removeAll(artist.getAlbums());
        MyDatabase.getInstance().getArtists().remove(artist);
    }

    /**
     * @param abstractUser
     */
    public static void removeHost(final AbstractUser abstractUser) {
        Host host = (Host) abstractUser;
        MyDatabase.getInstance().getPodcasts().removeAll(host.getPodcasts());
        MyDatabase.getInstance().getHosts().remove(host);
    }

    /**
     * @param album
     * @return likes no
     */
    public static int getAlbumsLikes(final Album album) {
        AtomicInteger likes = new AtomicInteger(0);
        album.getSongs().forEach(song -> likes.addAndGet(song.likesNo()));
        return likes.get();
    }

    /**
     * @param artist
     * @return
     */
    public static int getArtistLikes(final Artist artist) {
        AtomicInteger likes = new AtomicInteger(0);

        artist.getAlbums().forEach(album -> likes.addAndGet(getAlbumsLikes(album)));

        return likes.get();
    }

    public static boolean userInteractWithOther(final AbstractUser absUser) {
        List<User> listeningTo = AdminAPI.getUsersListeningToCreator((ContentCreator) absUser);

        List<User> usersHavingPage =
                AdminAPI.getOnlineUsers().stream().filter(user -> user.getPageHandler()
                        .getContentCreatorPage().equals(absUser.getUsername())).toList();

        List<User> usersHavingPageActive = usersHavingPage.stream()
                .filter(user -> user.getPageHandler().getCurrentPage().equals(EnumPages.HOST)
                        || user.getPageHandler().getCurrentPage().equals(EnumPages.ARTIST)).toList();
        if (!listeningTo.isEmpty() || !usersHavingPageActive.isEmpty()) {
            usersHavingPage.forEach(user -> user.getPageHandler().removeNonStandardPages());

            return true;
        }
        return false;
    }
}
