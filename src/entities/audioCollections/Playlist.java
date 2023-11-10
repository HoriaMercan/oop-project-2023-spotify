package entities.audioCollections;

import entities.audioFiles.Song;

import java.util.ArrayList;
import java.util.List;

public class Playlist extends AudioCollection {
	private boolean isPublic;
	private List<Song> songs = new ArrayList<>();

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

	public Playlist(String owner, boolean isPublic, List<Song>songs) {
		this.owner = owner;
		this.isPublic = isPublic;
		this.songs = songs;
	}

	public Playlist(String owner, boolean isPublic, List<Song>songs, String playlistName) {
		this(owner, isPublic, songs);
		this.name = playlistName;
	}

}
