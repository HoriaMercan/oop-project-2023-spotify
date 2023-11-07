package databases;

import entities.User;
import entities.audioColections.Playlist;
import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;
import java.util.List;

/**
 * This class stores the data about songs, podcast episodes
 * It contains data about users
 * It contains the available methods that can be performed by a user
 * Singleton pattern used
 * */
public class StaticDatabase {
	private final static StaticDatabase instance = new StaticDatabase();
	private StaticDatabase() {}

	private List<Song>songs;
	private List<PodcastEpisode>podcastEpisodes;
	private final List<User>users = null;
	private List<Playlist> publicPlaylists = null;

	public static StaticDatabase getInstance() {
		;
		return instance;
	}
	public void setSongs(final List<Song>songs) {
		this.songs = songs;
	}
	public List<Song> getSongs() {
		return songs;
	}

	public void setPodcastEpisodes(final List<PodcastEpisode>podcastEpisodes) {
		this.podcastEpisodes = podcastEpisodes;
	}
	public List<PodcastEpisode> getPodcastEpisodes() {
		return podcastEpisodes;
	}

	public void setPublicPlaylists(List<Playlist>playlists) {
		this.publicPlaylists = playlists;
	}
}
