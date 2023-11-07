package entities.audioFiles;

import java.util.List;

public class Song extends AudioFile{
	protected String album;
	protected List<String> tags;
	protected String lyrics;
	protected String genre;
	protected Integer releaseYear;
	protected String artist;

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

	public String getAlbum() {
		return album;
	}
	public List<String> GetTags() {
		return this.tags;
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
