package entities.audioFiles;

import entities.wrapper.Listenable;
import entities.wrapper.VisitorWrapper;
import fileio.input.SongInput;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public final class Song extends AudioFile implements Listenable {
    private final HashSet<String> usersLike = new HashSet<>();
    private final HashMap<String, Integer> userLikedAtTime = new HashMap<>();
    private String album;
    private List<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private String artist;

    public Song() {
        super();
    }

    public Song(final String name, final Integer duration, final String album,
                final List<String> tags, final String lyrics, final String genre,
                final Integer releaseYear, final String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.creator = this.artist;
    }

    public Song(final SongInput song) {
        this(song.getName(), song.getDuration(), song.getAlbum(), song.getTags(),
                song.getLyrics(), song.getGenre(), song.getReleaseYear(), song.getArtist());
    }

    /**
     * @param username username
     * @return checks whether a user liked this song or no
     */
    public boolean isSongLikedByUser(final String username) {
        return usersLike.contains(username);
    }

    /**
     * @param username user
     * @return returns the moment at which the song was liked
     */
    public Integer getTimestampOfLike(final String username) {
        return userLikedAtTime.get(username);
    }

    /**
     * Song gets liked by user
     *
     * @param username  user
     * @param timestamp time
     */
    public void songLikeByUser(final String username, final Integer timestamp) {
        usersLike.add(username);
        userLikedAtTime.put(username, timestamp);
    }

    /**
     * Song gets unliked by user
     *
     * @param username user
     * @return True if user liked the song before the function call
     */
    public boolean songUnlikeByUser(final String username) {
        if (usersLike.contains(username)) {
            userLikedAtTime.remove(username);
        }
        return usersLike.remove(username);
    }

    /**
     * @return Number of likes got by the song
     */
    public Integer likesNo() {
        return usersLike.size();
    }

    /**
     * @return list of usernames that liked this song
     */
    public List<String> userLikedThisSong() {
        return usersLike.stream().toList();
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(final List<String> tags) {
        this.tags = tags;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(final String album) {
        this.album = album;
    }

    public String getLyrics() {
        return this.lyrics;
    }

    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(final Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getArtist() {
        return artist;
    }

    /**
     * @param artist artist to be set for given song, here we define the creator
     */
    public void setArtist(final String artist) {
        this.artist = artist;
        creator = artist;
    }

    @Override
    public String toString() {
        return name + " - " + artist;
    }

    @Override
    public void acceptListen(final VisitorWrapper visitor) {
        visitor.visitListen(this);
    }
}
