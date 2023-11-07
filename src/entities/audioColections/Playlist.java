package entities.audioColections;

import entities.audioFiles.Song;

import java.util.List;

public class Playlist {
	private String owner;
	private boolean isPublic;
	private List<Song> songs;

	public Playlist(String owner, boolean isPublic, List<Song>songs) {
		this.owner = owner;
		this.isPublic = isPublic;
		this.songs = songs;
	}

	public void addSongToPlaylist(Song song) {
		songs.add(song);
	}
}
