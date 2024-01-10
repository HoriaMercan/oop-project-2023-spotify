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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Artist Wrapper Statistics module
 */
public final class ArtistWrapperStatistics extends WrapperStatistics {

    private final VisitorWrapper visitorAudioFile = new VisitorWrapper() {
        @Override
        public void visitListen(final User user) {
        }

        @Override
        public void visitListen(final Song song) {
            listenedSongs.compute(song, UPDATER);
            String album = song.getAlbum();

            if (album != null) {
                albumsListened.compute(album, STRING_UPDATER);
            }
        }

        @Override
        public void visitListen(final PodcastEpisode podcastEpisode) {
        }
    };

    private final Map<Song, Integer> listenedSongs = new HashMap<>();
    private final Map<String, Integer> albumsListened = new HashMap<>();
    private final Map<String, Integer> fans = new HashMap<>();

    /**
     * @return Artist Data Wrapping object
     */
    public AbstractDataWrapping getDataWrapping() {
        if (listenedSongs.isEmpty() && albumsListened.isEmpty() && fans.isEmpty()) {
            return null;
        }
        return ArtistDataWrapping.BUILDER
                .setTopSongs(transformToFormat(listenedSongs, AudioFile::getName))
                .setTopAlbums(transformToFormat(albumsListened, s -> s))
                .setTopFans(transformToFormatList(fans, s -> s))
                .setListeners(fans.size())
                .build();
    }

    /**
     * @param listen adds a listen to be counted
     */
    public void addOneListen(final OneListen listen) {
        listen.getAudioFile().acceptListen(this.visitorAudioFile);

        String username = listen.getUser().getUsername();
        fans.compute(username, STRING_UPDATER);

        listen.getUser().getPayment().addSong((Song) listen.getAudioFile());
    }

    /**
     * @return whether the artist was ever listened or not
     */
    public boolean wasEverListened() {
        return !listenedSongs.isEmpty();
    }

    /**
     * @return List of top 5 users listening to the artist
     */
    public List<User> getTopFans() {
        return transformToFormatList(fans, s -> s).stream()
                .map(s -> MyDatabase.getInstance().findUserByUsername(s)).toList();
    }

}
