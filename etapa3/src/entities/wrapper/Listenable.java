package entities.wrapper;

import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;

public interface Listenable {
    void acceptListen(VisitorWrapper visitor);
}
