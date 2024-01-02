package entities.wrapper;


import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;
import entities.users.Artist;
import entities.users.Host;
import entities.users.User;

/**
 * Visitor pattern used for wrapper: decide the user who will be using
 * the statistics
 */
public interface VisitorWrapper {
    void visitListen(User user);
//    void visitListen(Artist artist);
//    void visitListen(Host host);

    void visitListen(Song song);
    void visitListen(PodcastEpisode podcastEpisode);
}
