package entities.wrapper;


import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;
import entities.users.User;

/**
 * Visitor pattern used for wrapper: decide the user who will be using
 * the statistics
 */
public interface VisitorWrapper {
    /**
     * @param user Visitor pattern implementation for User
     */
    void visitListen(User user);

    /**
     * @param song Visitor pattern implementation for Song
     */
    void visitListen(Song song);
    /**
     * @param podcastEpisode Visitor pattern implementation for PodcastEpisode
     */
    void visitListen(PodcastEpisode podcastEpisode);
}
