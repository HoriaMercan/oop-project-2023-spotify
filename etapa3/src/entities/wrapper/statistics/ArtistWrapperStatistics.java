package entities.wrapper.statistics;

import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;
import entities.users.User;
import entities.wrapper.OneListen;
import entities.wrapper.VisitorWrapper;
import entities.wrapper.handlers.AbstractDataWrapping;

import java.util.HashMap;
import java.util.Map;

public class ArtistWrapperStatistics extends WrapperStatistics {

    private final VisitorWrapper visitorAudioFile = new VisitorWrapper() {
        @Override
        public void visitListen(User user) {
        }

        @Override
        public void visitListen(Song song) {
            listenedSongs.compute(song, updater);
            String album = song.getAlbum();

            if (album != null) {
                albumsListened.compute(album, stringUpdater);
            }
        }

        @Override
        public void visitListen(PodcastEpisode podcastEpisode) {
        }
    };

    Map<Song, Integer> listenedSongs = new HashMap<>();
    Map<String, Integer> albumsListened = new HashMap<>();
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
