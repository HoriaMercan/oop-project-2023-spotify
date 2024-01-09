package entities.wrapper.statistics;

import databases.MyDatabase;
import entities.audioFiles.AudioFile;
import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;
import entities.users.User;
import entities.wrapper.OneListen;
import entities.wrapper.VisitorWrapper;
import entities.wrapper.handlers.AbstractDataWrapping;
import entities.wrapper.handlers.ArtistDataWrapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

//    ArrayList<>

    public AbstractDataWrapping getDataWrapping() {
        if (listenedSongs.isEmpty() && albumsListened.isEmpty() && fans.isEmpty())
            return null;
        return ArtistDataWrapping.builder
                .setTopSongs(transformToFormat(listenedSongs, AudioFile::getName))
                .setTopAlbums(transformToFormat(albumsListened, s->s))
                .setTopFans(transformToFormatList(fans, s->s))
                .setListeners(fans.size())
                .build();
    }

    public void addOneListen(OneListen listen) {
        listen.getAudioFile().acceptListen(this.visitorAudioFile);

        String username = listen.getUser().getUsername();
        fans.compute(username, stringUpdater);

        listen.getUser().getPayment().addSong((Song) listen.getAudioFile());
    }

    public boolean wasEverListened() {
        return !listenedSongs.isEmpty();
    }

    public List<User> getTopFans() {
        return transformToFormatList(fans, s->s).stream()
                .map(s -> MyDatabase.getInstance().findUserByUsername(s)).toList();
    }

}
