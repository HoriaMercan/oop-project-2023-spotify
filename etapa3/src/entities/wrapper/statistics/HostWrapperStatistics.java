package entities.wrapper.statistics;

import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;
import entities.users.User;
import entities.wrapper.OneListen;
import entities.wrapper.VisitorWrapper;
import entities.wrapper.handlers.AbstractDataWrapping;

import java.util.HashMap;
import java.util.Map;

public class HostWrapperStatistics extends WrapperStatistics {

    private final VisitorWrapper visitorAudioFile = new VisitorWrapper() {
        @Override
        public void visitListen(User user) {
        }

        @Override
        public void visitListen(Song song) {

        }

        @Override
        public void visitListen(PodcastEpisode podcastEpisode) {
            listenedEpisodes.compute(podcastEpisode, updater);
        }
    };

    Map<PodcastEpisode, Integer> listenedEpisodes = new HashMap<>();
    Map<String, Integer> fans = new HashMap<>();

    public AbstractDataWrapping getDataWrapping() {
        return null;
    }

    public void addOneListen(OneListen listen) {
        listen.getAudioFile().acceptListen(visitorAudioFile);

        String username = listen.getUser().getUsername();
        fans.compute(username, stringUpdater);
    }

}

