package entities.audioCollections;

import entities.audioFiles.Song;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Playlist extends AudioCollection {
    private final Set<String> usersFollow = new HashSet<>();
    private boolean isPublic;
    private List<Song> songs = new ArrayList<>();
    private Integer playlistId;

    public Playlist(final String owner, final boolean isPublic) {
        this.owner = owner;
        this.isPublic = isPublic;
        this.songs = new ArrayList<>();
    }

    public Playlist(final String owner, final boolean isPublic, final List<Song> songs) {
        this(owner, isPublic);
        this.songs = songs;
    }

    public Playlist(final String owner, final boolean isPublic, final List<Song> songs,
                    final String playlistName) {
        this(owner, isPublic, songs);
        this.name = playlistName;
    }

    /**
     * This function performs the operation of a playlist getting followed by an user
     *
     * @param username username
     */
    public void getFollowedBy(final String username) {
        usersFollow.add(username);
    }

    /**
     * @return the followers number for the current playlist
     */
    public Integer followersNo() {
        return usersFollow.size();
    }

    /**
     * This function performs the operation in which a user unfollows a playlist
     *
     * @param username a username
     * @return True if the user followed the playlist before this function call
     */
    public boolean getUnfollowed(final String username) {
        return usersFollow.remove(username);
    }

    /**
     * @param username
     * @return verify if a user follows this playlist
     */
    public boolean isFollowedBy(final String username) {
        return usersFollow.contains(username);
    }

    public String getVisibility() {
        return isPublic ? "public" : "private";
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(final boolean aPublic) {
        isPublic = aPublic;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(final List<Song> songs) {
        this.songs = songs;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(final Integer playlistId) {
        this.playlistId = playlistId;
    }

    /**
     * Adds a song to the playlist
     *
     * @param song a Song object
     */
    public void addSongInPlaylist(final Song song) {
        songs.add(song);
    }

    /**
     * This function performs removing a song from the playlist
     *
     * @param song a Song object
     * @return True if the song could have been found in the playlist before the function call
     */
    public boolean removeSongFromPlaylist(final Song song) {
        return songs.remove(song);
    }

    @Override
    public List<Song> getAudioFiles() {
        return songs;
    }

    @Override
    public String toString() {
        return name + " - " + owner;
    }

}
