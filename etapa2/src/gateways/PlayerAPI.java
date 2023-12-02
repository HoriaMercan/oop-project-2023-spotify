package gateways;

import commands.player.StatusCommand.StatusOutput.Stats;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.users.User;
import entities.users.functionalities.UserPlayer;
import entities.audioCollections.Playlist;
import entities.audioCollections.Podcast;
import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;

import java.util.ArrayList;
import java.util.List;

public final class PlayerAPI {
    private static final MyDatabase DATABASE;

    static {
        DATABASE = MyDatabase.getInstance();
    }

    private PlayerAPI() {
    }

    private static List<Song> getSongListFromStrings(final String s) {
        List<Song> list = new ArrayList<Song>();
        list.add(DATABASE.findSongByName(s));
        return list;
    }

    private static List<Song> getSongListFromPlaylist(final Playlist p) {
        return new ArrayList<>(p.getSongs());
    }

    private static List<PodcastEpisode> getEpisodesFromPodcast(final Podcast p) {
        return new ArrayList<>(p.getEpisodes());
    }

    private static String modifyAndUpdatePlayer(final UserPlayer player, final Integer timestamp) {
        switch (player.getTypeSearched()) {
            case "song":
                player.setContext(getSongListFromStrings(player.getLastSelected()), timestamp);
                return "";

            case "playlist":
                Playlist playlist = DATABASE.findPlaylistByName(player.getLastSelected());
                if (playlist.getSongs().isEmpty()) {
                    return "You can't load an empty audio collection!";
                }
                player.setContext(getSongListFromPlaylist(playlist), timestamp);
                return "";

            case "podcast":
                Podcast podcast = DATABASE.findPodcastByName(player.getLastSelected());
                if (podcast.getEpisodes().isEmpty()) {
                    return "You can't load an empty audio collection!";
                }
                player.setContext(getEpisodesFromPodcast(podcast), timestamp);
                player.setPlayedPodcastName(podcast.getName());
                return "";
            case "album":
                Album album = DATABASE.findAlbumByName(player.getLastSelected());
                if (album == null || album.getSongs() == null || album.getSongs().isEmpty()) {
                    return "You can't load an empty audio collection!";
                }
                player.setContext(new ArrayList<>(album.getSongs()), timestamp);
                return "";
            default:
                break;
        }
        return "";
    }

    /**
     * This method loads the last selected file/collection selected by an user and
     * returns the equivalent message
     *
     * @param username  Username of the command initiator
     * @param timestamp Timestamp of command
     */
    public static String getLoadMessage(final String username, final Integer timestamp) {
        User user = DATABASE.findUserByUsername(username);
        UserPlayer player = user.getPlayer();

        if (player.getLastSelected().isEmpty()) {
            return "Please select a source before attempting to load.";
        }

        String res = modifyAndUpdatePlayer(player, timestamp);
        if (!res.isEmpty()) {
            return res;
        }

        player.loadPlayer(timestamp);
        player.setLastSelected("");
        player.setLastSearched(null);
        player.setTypeSearched("");
        return "Playback loaded successfully.";
    }

    /**
     * This function is used for generating the desired output
     * of status command by setting up all the parameters
     *
     * @param stats the status object to be set
     * @param username the username of the command initiator
     * @param timestamp the timestamp of the command
     */
    public static void setStatus(final Stats stats, final String username,
                                 final Integer timestamp) {
        User user = DATABASE.findUserByUsername(username);
        UserPlayer player = user.getPlayer();
        if (user.isOnline()) {
            player.updatePlayer(timestamp);
        }

        if (player.getTypeLoaded().isEmpty()) {
            stats.setName("");
            stats.setRemainedTime(0);
            stats.setPaused(true);
            stats.setShuffle(false);
            stats.setRepeat("No Repeat");
        } else {
            stats.setName(player.getCurrentPlayed().getName());
            stats.setRemainedTime(player.getRemainedTime());
            stats.setPaused(player.isPaused());
            stats.setShuffle(player.isShuffle());
            stats.setRepeat(player.getRepeatStatus());
        }


    }

    /**
     * This function execute a play/pause command and returns a desired message
     * or throws an error message regarding the action performed
     *
     * @param username the username of the command initiator
     * @param timestamp the timestamp of the command
     * @return A specific message correlated with the actions performed
     */
    public static String getPlayPauseMessage(final String username, final Integer timestamp) {
        User user = DATABASE.findUserByUsername(username);
        user.getPlayer().updatePlayer(timestamp);
        if (user.getPlayer().getTypeLoaded().isEmpty()) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        return user.getPlayer().playPause(timestamp);
    }

    /**
     * This function execute the specific actions in the database and returns
     * a message or returns the error got performing this action
     *
     * @param username username of the command initiator
     * @param playlistName playlist name chosen by an user
     * @return a String specifying the message of command
     */
    public static String getCreatePlaylistCommand(final String username,
                                                  final String playlistName) {
        User user = DATABASE.findUserByUsername(username);

        Playlist playlist = DATABASE.findPlaylistByName(playlistName);
        if (playlist != null) {
            return "A playlist with the same name already exists.";
        }

        playlist = new Playlist(username, true, new ArrayList<Song>());
        playlist.setName(playlistName);
        DATABASE.addPlaylistInDatabase(playlist);
        user.addPlaylistInUserList(playlistName);
        return "Playlist created successfully.";
    }

    /**
     * This function returns the audio file name which is played by an user at a certain
     * time
     *
     * @param username username of the command initiator
     * @param timestamp the timestamp of the command
     * @return a String specifying the message of command
     */
    public static String getCurrentPlayedType(final String username, final Integer timestamp) {
        User user = DATABASE.findUserByUsername(username);
        user.getPlayer().updatePlayer(timestamp);
        return user.getPlayer().getTypeLoaded();
    }

    /**
     * This function adds the current played song to a playlist specified by
     * a user
     *
     * @param username username of the command initiator
     * @param timestamp the timestamp of the command
     * @param playlistID the id associated with an user's playlist
     * @return a String specifying the message of command
     */
    public static String getAddRemoveMessage(final String username, final Integer timestamp,
                                             final Integer playlistID) {
        User user = DATABASE.findUserByUsername(username);
        user.getPlayer().updatePlayer(timestamp);
        if (user.getPlayer().getTypeLoaded().isEmpty()) {
            return "Please load a source before adding to or removing from the playlist.";
        }
        if (!user.getPlayer().getTypeLoaded().equals("song")) {
            return "The loaded source is not a song.";
        }
        if (!user.isPlaylistIDInUserList(playlistID)) {
            return "The specified playlist does not exist.";
        }

        Song song = (Song) user.getPlayer().getCurrentPlayed();
        Playlist playlist = DATABASE.findPlaylistByName(user.getPlaylistFromID(playlistID));
        if (playlist.removeSongFromPlaylist(song)) {
            return "Successfully removed from playlist.";
        }
        playlist.addSongInPlaylist(song);
        return "Successfully added to playlist.";
    }

    /**
     * This function execute a like command and returns a message
     *
     * @param username username of the command initiator
     * @param timestamp the timestamp of the command
     * @return a String specifying the message of command
     */
    public static String getLikeMessage(final String username, final Integer timestamp) {
        User user = DATABASE.findUserByUsername(username);
        String currentType = getCurrentPlayedType(username, timestamp);
        if (currentType.equals("song") || currentType.equals("playlist")) {
            Song song = (Song) user.getPlayer().getCurrentPlayed();
            if (song.songUnlikeByUser(username)) {
                user.getLikedSongs().remove(song.getName());
                return "Unlike registered successfully.";

            }
            song.songLikeByUser(username, timestamp);
            user.getLikedSongs().add(song.getName());
            return "Like registered successfully.";
        } else if (currentType.isEmpty()) {
            return "Please load a source before liking or unliking.";
        }
        return "Loaded source is not a song.";
    }
}
