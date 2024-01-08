package databases;

import entities.audioCollections.Album;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Artist;
import entities.users.Host;
import entities.users.User;
import entities.audioCollections.Playlist;
import entities.audioCollections.Podcast;
import entities.audioFiles.Song;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class stores the data about songs, podcast episodes
 * It contains data about users
 * It contains the available methods that can be performed by a user
 * Singleton pattern used
 */
public final class MyDatabase {
    private static final MyDatabase INSTANCE = new MyDatabase();

    @Getter
    @Setter
    private ArrayList<Artist> artists = new ArrayList<>();

    @Getter
    @Setter
    private ArrayList<Host> hosts = new ArrayList<>();

    @Setter
    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<User> users;
    private ArrayList<Playlist> playlists = new ArrayList<>();

    @Setter
    @Getter
    private Map<User, List<Song>> unpayedSongs;


    @Getter
    private Song adBreak = null;

    private MyDatabase() {
    }

    public static MyDatabase getInstance() {
        return INSTANCE;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(final ArrayList<Song> songs) {
        this.songs = songs;
    }

    /**
     * This function is used to transform input data (songs) to Song object
     *
     * @param songs0 Song input array
     */
    public void setSongsConvert(final ArrayList<SongInput> songs0) {
        ArrayList<Song> newSongs = new ArrayList<>();
        for (SongInput song : songs0) {
            newSongs.add(new Song(song));
        }
        setSongs(newSongs);

        adBreak = this.songs.stream().filter(song -> song.getName().equals("Ad Break"))
                .toList().get(0);
    }

    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(final ArrayList<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    /**
     * This function is used to transform input data (PodcastInput) to Podcast object
     *
     * @param podcasts0 Podcast Input List
     */
    public void setPodcastsConvert(final ArrayList<PodcastInput> podcasts0) {
        ArrayList<Podcast> newPodcasts = new ArrayList<Podcast>();
        for (PodcastInput podcast : podcasts0) {
            newPodcasts.add(new Podcast(podcast, podcast.getOwner()));
        }
        this.podcasts = newPodcasts;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    /**
     * @param inputs user input array
     */
    public void setUsersConvert(final ArrayList<UserInput> inputs) {
        ArrayList<User> users0 = new ArrayList<>();
        for (UserInput input : inputs) {
            users0.add(new User(input));
        }
        this.users = users0;
    }

    public ArrayList<Playlist> getPublicPlaylists() {
        return playlists;
    }

    public void setPublicPlaylists(final ArrayList<Playlist> playlists0) {
        this.playlists = playlists0;
    }

    /**
     * @param playlist add a playlist to database
     */
    public void addPlaylistInDatabase(final Playlist playlist) {
        playlists.add(playlist);
    }

    /**
     * @param username String username
     * @return User associated \w given username
     */
    public User findUserByUsername(final String username) {
        for (AbstractUser user : this.users) {
            if (user.getUsername().equals(username)) {
                return user.getUserType().equals(UserType.NORMAL) ? (User) user : null;
            }
        }
        return null;
    }

    /**
     * @param username String username
     * @return Artist associated \w given username
     */
    public Artist findArtistByUsername(final String username) {
        for (Artist user : this.artists) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * @param username String username
     * @return Host associated \w given username
     */
    public Host findHostByUsername(final String username) {
        for (Host user : this.hosts) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * @param name Song name
     * @return the Song associated with name
     */
    public Song findSongByName(final String name) {
        List<Song> songList = this.songs.stream().
                filter(song -> song.getName().equals(name)).toList();
        if (songList.isEmpty()) {
            return null;
        }
        return songList.get(0);
    }

    /**
     * @param name Playlist name
     * @return the playlist associated \w name
     */
    public Playlist findPlaylistByName(final String name) {
        List<Playlist> pl = this.playlists.stream().
                filter(playlist -> playlist.getName().equals(name)).toList();
        if (pl.isEmpty()) {
            return null;
        }
        return pl.get(0);
    }

    /**
     * @param name podcast name
     * @return a podcast associated \w the name
     */
    public Podcast findPodcastByName(final String name) {
        List<Podcast> p = this.podcasts.stream().
                filter(podcast -> podcast.getName().equals(name)).toList();

        if (p.isEmpty()) {
            return null;
        }
        return p.get(0);
    }

    /**
     * @param name album name
     * @return an album associated \w the name
     */
    public Album findAlbumByName(final String name) {
        List<Album> ans = albums.stream().filter(album -> album.getName().equals(name))
                .toList();

        return ans.isEmpty() ? null : ans.get(0);
    }

    /**
     * @param user AbstractUser object that has to be added to the database
     */
    public void addUser(final AbstractUser user) {
        if (user == null) {
            return;
        }
        switch (user.getUserType()) {
            case NORMAL -> users.add((User) user);
            case ARTIST -> artists.add((Artist) user);
            case HOST -> hosts.add((Host) user);
            default -> {
            }
        }
    }

    /**
     * @param username
     * @return return an AbstractUser object \w given username
     */
    public AbstractUser findAbstractUserByUsername(final String username) {
        List<AbstractUser> allUsers = new ArrayList<>();
        allUsers.addAll(users.stream().map(user -> (AbstractUser) user).toList());
        allUsers.addAll(artists.stream().map(user -> (AbstractUser) user).toList());
        allUsers.addAll(hosts.stream().map(user -> (AbstractUser) user).toList());

        List<AbstractUser> ans = allUsers.stream()
                .filter(abstractUser -> abstractUser.getUsername().equals(username))
                .toList();

        return ans.isEmpty() ? null : ans.get(0);
    }

    /**
     * @return List of all usernames of all users from the database
     */
    public List<String> getAllAbstractUserNames() {
        List<String> answer = new ArrayList<>();
        answer.addAll(users.stream().map(AbstractUser::getUsername).toList());
        answer.addAll(artists.stream().map(AbstractUser::getUsername).toList());
        answer.addAll(hosts.stream().map(AbstractUser::getUsername).toList());

        return answer;
    }

}
