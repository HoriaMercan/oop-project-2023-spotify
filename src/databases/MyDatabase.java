package databases;

import entities.User;
import entities.audioCollections.Playlist;
import entities.audioCollections.Podcast;
import entities.audioFiles.Song;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the data about songs, podcast episodes
 * It contains data about users
 * It contains the available methods that can be performed by a user
 * Singleton pattern used
 * */
public class MyDatabase {
	private final static MyDatabase instance = new MyDatabase();

	private ArrayList<Song> songs;
	private ArrayList<Podcast> podcasts;
	private ArrayList<User> users;

	private ArrayList<Playlist>playlists = new ArrayList<>();
	private MyDatabase() {}

	public static MyDatabase getInstance() {
		return instance;
	}

	public ArrayList<Song> getSongs() {
		return songs;
	}

	public void setSongs(ArrayList<Song> songs) {
		this.songs = songs;
	}

	public void setSongsConvert(ArrayList<SongInput>songs) {
		ArrayList<Song>newSongs = new ArrayList<Song>();
		for (SongInput song: songs) {
			newSongs.add(new Song(song));
		}
		setSongs(newSongs);
	}

	public ArrayList<Podcast> getPodcasts() {
		return podcasts;
	}

	public void setPodcasts(ArrayList<Podcast> podcasts) {
		this.podcasts = podcasts;
	}

	public void setPodcastsConvert(ArrayList<PodcastInput> podcasts) {
		ArrayList<Podcast>newPodcasts = new ArrayList<Podcast>();
		for (PodcastInput podcast: podcasts) {
			newPodcasts.add(new Podcast(podcast));
		}
		this.podcasts = newPodcasts;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public void setUsersConvert(ArrayList<UserInput> inputs) {
		ArrayList<User>users = new ArrayList<>();
		for (UserInput input: inputs) {
			users.add(new User(input));
		}
		this.users = users;
	}

	public ArrayList<Playlist> getPublicPlaylists() {
		return playlists;
	}

	public void setPublicPlaylists(ArrayList<Playlist> playlists) {
		this.playlists = playlists;
	}

	public User findUserByUsername(String username) {
		for (User user: this.users) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public Song findSongByName(String name) {
		List<Song> songs = this.songs.stream().
				filter(song -> song.getName().equals(name)).toList();
		if (songs.isEmpty()) {
			return null;
		}
		return songs.get(0);
	}

	public Playlist findPlaylistByName(String name) {
		List<Playlist> pl = this.playlists.stream().
				filter(playlist -> playlist.getName().equals(name)).toList();
		if (pl.isEmpty()) {
			return null;
		}
		return pl.get(0);
	}

	public Podcast findPodcastByName(String name) {
		List<Podcast> p = this.podcasts.stream().
				filter(podcast -> podcast.getName().equals(name)).toList();

		if (p.isEmpty())
			return null;
		return p.get(0);
	}
}
