package entities.audioFiles;

import fileio.input.SongInput;

import java.util.HashSet;
import java.util.List;

public class Song extends AudioFile{
	protected String album;
	protected List<String> tags;
	protected String lyrics;
	protected String genre;
	protected Integer releaseYear;
	protected String artist;

	protected HashSet<String> usersLike = new HashSet<>();

	public boolean isSongLikedByUser(String username) {
		return usersLike.contains(username);
	}

	public void songLikeByUser(String username) {
		usersLike.add(username);
	}

	public boolean songUnlikeByUser(String username) {
		return usersLike.remove(username);
	}

	public Song() {
		super();
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}


	public Song(String name, Integer duration, String album, List<String>tags,
				String lyrics, String genre, Integer releaseYear, String artist) {
		super(name, duration);
		this.album = album;
		this.tags = tags;
		this.lyrics = lyrics;
		this.genre = genre;
		this.releaseYear = releaseYear;
		this.artist = artist;
	}
	public Song(SongInput song) {
		this(song.getName(), song.getDuration(), song.getAlbum(), song.getTags(),
				song.getLyrics(), song.getGenre(), song.getReleaseYear(), song.getArtist());
	}


	public String getAlbum() {
		return album;
	}
	public String getLyrics() {
		return this.lyrics;
	}
	public String getGenre() {
		return this.genre;
	}
	public Integer getReleaseYear() {
		return releaseYear;
	}
	public String getArtist() {
		return artist;
	}

}
