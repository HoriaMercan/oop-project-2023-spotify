package gateways;

import entities.User.UserPlayer;
import entities.audioCollections.Podcast;
import entities.audioFiles.AudioFile;
import entities.helpers.Filter;
import databases.MyDatabase;
import entities.User;
import entities.audioCollections.Playlist;
import entities.audioFiles.Song;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class SearchBarAPI {
    private static final MyDatabase MY_DATABASE = MyDatabase.getInstance();
    private static final int LIMIT = 5;
    private SearchBarAPI() {
    }

    private static <T> boolean isIncluded(final List<T> list, final List<T> sublist) {
        Set<T> set = new HashSet<>(list);
        set.addAll(new ArrayList<>(sublist));
        return set.size() == list.size();
    }

    private static boolean isRelativeToReleaseYear(final Integer releaseYear, final String s) {
        if (s.length() < 2) {
            return true;
        }
        return switch (s.substring(0, 1)) {
            case "<" -> releaseYear < Integer.parseInt(s.substring(1));
            case ">" -> releaseYear > Integer.parseInt(s.substring(1));
            default -> false;
        };
    }

    /**
     * This function execute a search for songs by considering a filter
     *
     * @param username username of command initiator
     * @param timestamp timestamp of command
     * @param filter the filter used in search
     * @return a list of songs name
     */
    public static List<String> getSongsByFilter(final String username, final Integer timestamp,
                                                final Filter filter) {

        List<Song> songs = MY_DATABASE.getSongs();
        List<String> resultSongs;

        Predicate<Song> byTag = song -> filter.getTags().isEmpty()
                || isIncluded(song.getTags(), filter.getTags());
        Predicate<Song> byGenre = song ->
                song.getGenre().toLowerCase().startsWith(filter.getGenre().toLowerCase());
        Predicate<Song> byName = song -> song.getName().startsWith(filter.getName());
        Predicate<Song> byLyrics = song -> song.getLyrics().toLowerCase()
                .contains(filter.getLyrics().toLowerCase());
        Predicate<Song> byAlbum = song -> song.getAlbum().startsWith(filter.getAlbum());
        Predicate<Song> byArtist = song -> filter.getArtist().isEmpty()
                || song.getArtist().equalsIgnoreCase(filter.getArtist());
        Predicate<Song> byReleaseYear = song ->
                isRelativeToReleaseYear(song.getReleaseYear(), filter.getReleaseYear());
        Function<Song, String> songToName = AudioFile::getName;

        resultSongs = songs.stream().filter(byTag).filter(byGenre).filter(byName)
                .filter(byLyrics).filter(byAlbum).filter(byArtist)
                .filter(byReleaseYear)
                .map(songToName).toList()
                .stream().limit(LIMIT).toList();

        User user = MY_DATABASE.findUserByUsername(username);
        if (user != null) {
            user.getPlayer().setLastSearched(resultSongs);
            user.getPlayer().setTypeSearched("song");
            user.getPlayer().setLastSelected("");
            user.getPlayer().unsetContext(timestamp);
        }
        return resultSongs;
    }

    /**
     * This function filters some playlists and returns the name of
     * the first 5 matches
     *
     * @param username  the username of command initiator
     * @param timestamp the timestamp of command
     * @param filter    the Filter object used to filter objects
     * @return a list of string which matches the desired objects name
     */
    public static List<String> getPlaylistsByFilter(final String username,
                                                    final Integer timestamp,
                                                    final Filter filter) {
        List<String> resultPlaylists = new ArrayList<>();
        User user = MY_DATABASE.findUserByUsername(username);
        if (user == null) {
            return resultPlaylists;
        }
        List<Playlist> playlists = MY_DATABASE.getPublicPlaylists();

        Predicate<Playlist> byPermission = playlist -> playlist.isPublic()
                || playlist.getOwner().equals(username);
        Predicate<Playlist> byName = playlist -> playlist.getName().contains(filter.getName());
        Predicate<Playlist> byOwner = playlist -> filter.getOwner().isEmpty()
                || playlist.getOwner().equals(filter.getOwner());

        Function<Playlist, String> playlistToName = Playlist::getName;

        resultPlaylists = playlists.stream().filter(byPermission)
                .filter(byName).filter(byOwner)
                .map(playlistToName).toList()
                .stream().limit(LIMIT).toList();

        user.getPlayer().setLastSearched(resultPlaylists);
        user.getPlayer().setTypeSearched("playlist");
        user.getPlayer().setLastSelected("");
        user.getPlayer().unsetContext(timestamp);
        return resultPlaylists;
    }

    /**
     * This function filters some podcasts and returns the name of
     * the first 5 matches
     *
     * @param username  the username of command initiator
     * @param timestamp the timestamp of command
     * @param filter    the Filter object used to filter objects
     * @return a list of string which matches the desired objects name
     */
    public static List<String> getPodcastsByFilter(final String username,
                                                   final Integer timestamp, final Filter filter) {
        List<String> resultPodcasts;

        List<Podcast> podcasts = MY_DATABASE.getPodcasts();

        Predicate<Podcast> byName = podcast -> podcast.getName().startsWith(filter.getName());
        Predicate<Podcast> byOwner = podcast -> podcast.getOwner().startsWith(filter.getOwner());

        Function<Podcast, String> podcastToName = Podcast::getName;
        resultPodcasts = podcasts.stream().filter(byName).filter(byOwner)
                .map(podcastToName).toList()
                .stream().limit(LIMIT).toList();

        User user = MY_DATABASE.findUserByUsername(username);
        if (user != null) {
            user.getPlayer().setLastSearched(resultPodcasts);
            user.getPlayer().setTypeSearched("podcast");
            user.getPlayer().setLastSelected("");
            user.getPlayer().unsetContext(timestamp);
        }
        return resultPodcasts;
    }


    /**
     * This function execute a selection on desired item and returns a message
     *
     * @param username   the username of the command initiator
     * @param itemNumber the item index from last search
     * @return a message which consists of error/success
     */
    public static String getSelectionMessage(final String username, final Integer itemNumber) {
        UserPlayer player = MY_DATABASE.findUserByUsername(username).getPlayer();
        List<String> lastSearched = player.getLastSearched();
        if (lastSearched == null) {
            return "Please conduct a search before making a selection.";
        }
        if (lastSearched.size() < itemNumber) {
            return "The selected ID is too high.";
        }


        player.setLastSelected(lastSearched.get(itemNumber - 1));
        return "Successfully selected " + player.getLastSelected() + ".";
    }
}
