package entities.wrapper.statistics;

import entities.audioFiles.AudioFile;
import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;
import entities.users.User;
import entities.wrapper.OneListen;
import entities.wrapper.VisitorWrapper;
import entities.wrapper.handlers.AbstractDataWrapping;
import entities.wrapper.handlers.HostDataWrapping;

import java.util.HashMap;
import java.util.Map;

public final class HostWrapperStatistics extends WrapperStatistics {

    private final VisitorWrapper visitorAudioFile = new VisitorWrapper() {
        @Override
        public void visitListen(final User user) {
        }

        @Override
        public void visitListen(final Song song) {

        }

        @Override
        public void visitListen(final PodcastEpisode podcastEpisode) {
            listenedEpisodes.compute(podcastEpisode, UPDATER);
        }
    };

    private Map<PodcastEpisode, Integer> listenedEpisodes = new HashMap<>();
    private Map<String, Integer> fans = new HashMap<>();

    /**
     * @return Host DataWrapping
     */
    public AbstractDataWrapping getDataWrapping() {
        if (listenedEpisodes.isEmpty() && fans.isEmpty()) {
            return null;
        }
        return HostDataWrapping.BUILDER
                .setTopEpisodes(transformToFormat(listenedEpisodes, AudioFile::getName))
                .setTopFans(transformToFormatList(fans, s -> s))
                .setListeners(fans.size())
                .build();

    }

    /**
     * @param listen adds a listen to be counted
     */
    public void addOneListen(final OneListen listen) {
        listen.getAudioFile().acceptListen(visitorAudioFile);

        String username = listen.getUser().getUsername();
        fans.compute(username, STRING_UPDATER);
    }

}

