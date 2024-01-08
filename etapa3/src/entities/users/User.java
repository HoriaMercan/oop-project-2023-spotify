package entities.users;

import databases.MyDatabase;
import entities.audioCollections.Playlist;
import entities.audioFiles.Song;
import entities.monetization.UserPayment;
import entities.users.functionalities.PageHandler;
import entities.users.functionalities.UserPlayer;
import entities.wrapper.statistics.UserWrapperStatistics;
import fileio.input.UserInput;
import lombok.Getter;
import pagesystem.EnumPages;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                                System.out.println(newSelection);
                                System.out.println(MyDatabase.getInstance()
                                        .findArtistByUsername(newSelection));
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
