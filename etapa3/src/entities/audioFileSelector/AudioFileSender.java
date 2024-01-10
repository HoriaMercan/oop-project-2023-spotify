package entities.audioFileSelector;

/**
 * send messages to the User and Content Creators
 * to register and monetize a Song/PodcastEpisode
 */
@FunctionalInterface
public interface AudioFileSender {
    /**
     * the send function
     */
    void send();
}
