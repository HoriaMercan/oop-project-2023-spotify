package entities.audioCollections;

import entities.audioFiles.Song;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Playlist extends AudioCollection {
	private boolean isPublic;
	private List<Song> songs = new ArrayList<>();

	private HashSet<String> usersFollow = new HashSet<>();

	public String getVisibility() {
		return isPublic ? "public" : "private";
	}
	public Integer getFollowersNumber() {
		return usersFollow.size();
	}

	public void addFollower(String username) {
		usersFollow.add(username);
	}

	public boolean removeFollower(String username) {
		return usersFollow.remove(username);
	}
	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean aPublic) {
		isPublic = aPublic;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public Integer getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(Integer playlistId) {
		this.playlistId = playlistId;
	}

	private Integer playlistId;

	public Playlist(String owner, boolean isPublic) {
		this.owner = owner;
		this.isPublic = isPublic;
		this.songs = new ArrayList<>();
	}
	public Playlist(String owner, boolean isPublic, List<Song>songs) {
		this(owner, isPublic);
		this.songs = songs;
	}

	public Playlist(String owner, boolean isPublic, List<Song>songs, String playlistName) {
		this(owner, isPublic, songs);
		this.name = playlistName;
	}

	public void addSongInPlaylist(Song song) {
		songs.add(song);
	}
	public boolean removeSongFromPlaylist(Song song) {
		return songs.remove(song);
	}

}
