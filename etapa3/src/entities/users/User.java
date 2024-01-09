package entities.users;

import databases.MyDatabase;
import entities.audioCollections.Playlist;
import entities.audioFiles.Song;
import entities.monetization.UserPayment;
import entities.users.functionalities.PageHandler;
import entities.users.functionalities.UserPlayer;
import entities.wrapper.statistics.ArtistWrapperStatistics;
import entities.wrapper.statistics.UserWrapperStatistics;
import fileio.input.UserInput;
import gateways.AdminAPI;
import gateways.PlayerAPI;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import pagesystem.EnumPages;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class User extends AbstractUser {
    private final UserPlayer player = new UserPlayer(this);
    @Getter
    private final UserPayment payment = new UserPayment(this);
    @Getter
    private final PageHandler pageHandler = new PageHandler();
    @Getter
    private final List<Song> likedSongs = new ArrayList<>();
    @Getter
    private final List<String> followedPlaylists = new ArrayList<>();
    private final HashMap<Integer, String> userPlaylists = new HashMap<>();
    private boolean isOnline = true;

    {
        pageHandler.addPage(EnumPages.HOME, User.this::getHomePage);
        pageHandler.addPage(EnumPages.LIKED_CONTENT, User.this::getLikedPage);
    }

    public User(final UserInput input) {
        this(input.getUsername(), input.getCity(), input.getAge());
        wrapperStatistics = new UserWrapperStatistics();
        player.setWrapperStatistics(wrapperStatistics);
    }

    public User(final String username, final String city, final Integer age) {
        this.username = username;
        this.city = city;
        this.age = age;
        userType = UserType.NORMAL;
        wrapperStatistics = new UserWrapperStatistics();
        player.setWrapperStatistics(wrapperStatistics);
    }

    public User() {
    }

    /**
     * @param playlistName playlist name
     * @return True if the given playlistName already exists in the user's associated
     * list of playlists
     */
    public boolean isPlaylistInUserList(final String playlistName) {
        return userPlaylists.containsValue(playlistName);
    }

    /**
     * Creates a new playlist associated with the user
     *
     * @param playlistName A new playlist to be created
     */
    public void addPlaylistInUserList(final String playlistName) {
        userPlaylists.put(userPlaylists.size() + 1, playlistName);
    }

    /**
     * @param id playlistID
     * @return True if the given playlistID already exists in the user's associated
     * *          list of playlists
     */
    public boolean isPlaylistIDInUserList(final Integer id) {
        return userPlaylists.containsKey(id);
    }

    /**
     * @param id a playlistID
     * @return the playlist name associated with the playlist id
     */
    public String getPlaylistFromID(final Integer id) {
        return userPlaylists.get(id);
    }

    public UserPlayer getPlayer() {
        return player;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(final boolean online) {
        isOnline = online;
    }

    /**
     *
     */
    public String changeLastSelected(final String newSelection) {

        String ans = "";
        if (!newSelection.isEmpty() && ((this.player.getTypeSearched().equals("artist"))
                || this.player.getTypeSearched().equals("host"))) {
            this.pageHandler.removePage(EnumPages.ARTIST);
            this.pageHandler.removePage(EnumPages.HOST);
            this.pageHandler.setContentCreatorPage(newSelection);
            switch (this.player.getTypeSearched()) {
                case "artist":

                    this.pageHandler.addPage(EnumPages.ARTIST,
                            () -> {
                                return MyDatabase.getInstance()
                                        .findArtistByUsername(newSelection)
                                        .getPageContent();
                            });
                    this.pageHandler.setCurrentPage(EnumPages.ARTIST);
                    break;
                case "host":

                    this.pageHandler.addPage(EnumPages.HOST,
                            () -> MyDatabase.getInstance().findHostByUsername(newSelection)
                                    .getPageContent());
                    this.pageHandler.setCurrentPage(EnumPages.HOST);
                    break;
                default:
            }
            ans = "Successfully selected " + newSelection + "'s page.";
        }
        this.player.setLastSelected(newSelection);
        return ans;
    }

    @Getter
    @Setter
    private LinkedList<Song> songRecommendations = new LinkedList<>();

    @Getter
    @Setter
    private LinkedList<String> playlistRecommendations = new LinkedList<>();

    private String lastRecommendation = "";

    private boolean randomSongUsed = false, randomPlaylistUsed = false,
            fansPlaylistUsed = false;

    public boolean updateSongRecommendations() {
        if (this.player.getCurrentAudioFileTime() < 30) {
            return false;
        }

        Song current = (Song) this.player.getCurrentPlayed();

        songRecommendations.add(
                PlayerAPI.getSongRecommendation(current.getGenre(),
                        this.player.getCurrentAudioFileTime(), current));

        lastRecommendation = "random_song";
        return true;
    }

    public boolean updateRandomPlaylistRecommendations() {
        if (randomPlaylistUsed) {
            return false;
        }

        randomPlaylistUsed = true;
        Map<String, Integer> topGenres = new HashMap<>();
        Set<Song> uniqueSongs = new HashSet<>(likedSongs);

        BiFunction<String, Integer, Integer> updateVal = (k, v) -> v == null ? 1 : 1 + v;

        for (Song song : likedSongs) {
            topGenres.compute(song.getGenre(), updateVal);
        }

        List<Playlist> myPlaylists = new ArrayList<>();

        myPlaylists.addAll(followedPlaylists.stream()
                .map(s -> MyDatabase.getInstance().findPlaylistByName(s)).toList());

        myPlaylists.addAll(userPlaylists.values().stream()
                .map(s -> MyDatabase.getInstance().findPlaylistByName(s))
                .toList());

        myPlaylists.forEach(playlist -> playlist.getSongs().forEach(song -> {
            if (uniqueSongs.contains(song)) {return;}

            uniqueSongs.add(song);
            topGenres.compute(song.getGenre(), updateVal);
        }));

        List<Entry<String, Integer>> array = new ArrayList<>(topGenres.entrySet());
        Collections.sort(array, (kIntegerEntry, t1) -> {
            if ((int) kIntegerEntry.getValue() != t1.getValue()) {
                return -Integer.compare(kIntegerEntry.getValue(), t1.getValue());
            }
            return kIntegerEntry.getKey().compareTo(t1.getKey());
        });

        List<String> topGenresList = array.stream().map(Entry::getKey).limit(3).toList();

        int initialValue = 5;

        List<Song> result = new ArrayList<>();

        for (String genre: topGenresList) {
            List<Song> auxSongs = new ArrayList<>(uniqueSongs.stream()
                    .filter(song -> song.getGenre().equals(genre))
                    .toList());

            auxSongs.sort((song, t1) -> {
                if (song.likesNo() > t1.likesNo()) {
                    return -1;
                }
                if (song.likesNo().equals(t1.likesNo())) {
                    return 0;
                }
                return 1;
            });

            result.addAll(auxSongs.stream().limit(initialValue).toList());

            initialValue--;
        }

        if (result.isEmpty()) {
            return false;
        }
        Playlist playlist = new Playlist(this.username, false, result);
        playlist.setName(this.username + "'s recommendations");
        MyDatabase.getInstance().addPlaylistInDatabase(playlist);

        playlistRecommendations.add(playlist.getName());
        lastRecommendation = "random_playlist";

        return true;
    }

    public boolean updateFansPlaylistRecommendations() {
        if (fansPlaylistUsed) {
            return false;
        }

        fansPlaylistUsed = true;
        String artistName = ((Song) this.player.getCurrentPlayed()).getArtist();
        List<User> topFans = ((ArtistWrapperStatistics)MyDatabase.getInstance()
                .findArtistByUsername(artistName)
                .getWrapperStatistics()).getTopFans();

        List<Song> result = new ArrayList<>();

        for (User fan: topFans) {
            List<Song> auxSongs = new ArrayList<>(fan.likedSongs);

            auxSongs.sort((song, t1) -> {
                if (song.likesNo() > t1.likesNo()) {
                    return -1;
                }
                if (song.likesNo().equals(t1.likesNo())) {
                    return 0;
                }
                return 1;
            });

            int songsAdded = 0;

            for (Song song : auxSongs) {
                if (!result.contains(song)) {
                    result.add(song);
                    songsAdded++;
                }
                if (songsAdded == 5) {
                    break;
                }
            }
        }

        if (result.isEmpty()) {
            return false;
        }
        Playlist playlist = new Playlist(this.username, false, result);
        playlist.setName(artistName + " Fan Club recommendations");
        MyDatabase.getInstance().addPlaylistInDatabase(playlist);

        playlistRecommendations.add(playlist.getName());
        lastRecommendation = "fans_playlist";

        return true;
    }

    public void loadRecommendation() {
        List<Song> toUpload = new ArrayList<>();
        Integer timestamp = this.player.getLastUpdatedTime();
        switch (lastRecommendation) {
            case "" : {
                break;
            }
            case "random_song": {
                toUpload.add(songRecommendations.getLast());
                this.player.setContext(toUpload, timestamp);

                player.loadPlayer(timestamp, "song");
                player.setLastSelected("");
                player.setLastSearched(null);
                player.setTypeSearched("");
                break;
            }
            default: {
                toUpload.addAll(MyDatabase.getInstance()
                        .findPlaylistByName(playlistRecommendations.getLast()).getSongs());

                this.player.setContext(toUpload, playlistRecommendations.getLast(), timestamp);

                player.loadPlayer(timestamp, "playlist");
                player.setLastSelected("");
                player.setLastSearched(null);
                player.setTypeSearched("");
                break;
            }
        }
    }
    /**
     * @return Home page content specific for every user
     */
    public String getHomePage() {
        StringBuilder sb = new StringBuilder("Liked songs:\n\t[");

        List<Song> newSongs = new ArrayList<>(likedSongs);
        if (!newSongs.isEmpty()) {
            newSongs.sort((song, t1) -> Integer.compare(t1.likesNo(), song.likesNo()));

            final int limit = 5;
            newSongs = newSongs.stream().limit(limit).toList();
            for (Song song : newSongs) {
                sb.append(song.getName()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]\n\nFollowed playlists:\n\t[");

        if (!followedPlaylists.isEmpty()) {
            for (String pl : followedPlaylists) {
                sb.append(pl).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]\n\nSong recommendations:\n\t[");

        // TODO: add song recommendation

        if (!songRecommendations.isEmpty()) {
            for(Song song: songRecommendations) {
                sb.append(song.getName()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]\n\nPlaylists recommendations:\n\t[");

        // TODO: add playlists recommendation
        if (!playlistRecommendations.isEmpty()) {
            for (String pl : playlistRecommendations) {
                sb.append(pl).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * @return Liked contents page content specific for every user
     */
    public String getLikedPage() {
        StringBuilder sb = new StringBuilder("Liked songs:\n\t[");

        if (!likedSongs.isEmpty()) {
            List<Song> songs = likedSongs;

            for (Song song : songs) {
                sb.append(song).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]\n\nFollowed playlists:\n\t[");

        if (!followedPlaylists.isEmpty()) {
            List<Playlist> playlists = followedPlaylists.stream()
                    .map(f -> MyDatabase.getInstance().findPlaylistByName(f)).toList();
            for (Playlist pl : playlists) {
                sb.append(pl).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }
}
