package entities.wrapper.statistics;

import databases.MyDatabase;
import entities.audioFiles.AudioFile;
import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;
import entities.users.Artist;
import entities.users.Host;
import entities.users.User;
import entities.wrapper.OneListen;
import entities.wrapper.VisitorWrapper;
import entities.wrapper.handlers.AbstractDataWrapping;
import entities.wrapper.handlers.UserDataWrapping;

import java.util.HashMap;
import java.util.Map;

/**
 * User wrapper statistics object stores the interactions of an user \w the audiofiles
 */
public final class UserWrapperStatistics extends WrapperStatistics {

    private final Map<Song, Integer> listenedSongs = new HashMap<>();
    private final Map<PodcastEpisode, Integer> listenedEpisodes = new HashMap<>();
    private final Map<String, Integer> artistsListened = new HashMap<>();
    private final Map<String, Integer> genresListened = new HashMap<>();
    private final Map<String, Integer> albumsListened = new HashMap<>();
    private OneListen lastListen = null;
    private final VisitorWrapper visitorAudioFile = new VisitorWrapper() {
        @Override
        public void visitListen(final User user) {

        }

        @Override
        public void visitListen(final Song song) {
            listenedSongs.compute(song, UPDATER);
            String artistName = song.getArtist();
            String genre = song.getGenre();
            String albumName = song.getAlbum();

            if (artistName != null) {
                artistsListened.compute(artistName, STRING_UPDATER);
            }
            if (genre != null) {
                genresListened.compute(genre, STRING_UPDATER);
            }

            if (albumName != null) {
                albumsListened.compute(albumName, STRING_UPDATER);
            }
            // ToDo: add to artists statistics

            Artist artist = MyDatabase.getInstance().findArtistByUsername(artistName);
            if (artist == null) {
                return;
            }

            artist.getWrapperStatistics().addOneListen(lastListen);

        }

        @Override
        public void visitListen(final PodcastEpisode podcastEpisode) {
            listenedEpisodes.compute(podcastEpisode, UPDATER);

            // ToDo: add to host statistics
            Host host = MyDatabase.getInstance()
                    .findHostByUsername(podcastEpisode.getCreator());

            if (host == null) {
                return;
            }

            host.getWrapperStatistics().addOneListen(lastListen);
        }
    };


    @Override
    public AbstractDataWrapping getDataWrapping() {
        if (listenedSongs.isEmpty() && artistsListened.isEmpty()
                && genresListened.isEmpty() && albumsListened.isEmpty()
                && listenedEpisodes.isEmpty()) {
            return null;
        }
        return UserDataWrapping.BUILDER
                .setTopSongs(transformToFormat(listenedSongs, AudioFile::getName))
                .setTopArtists(transformToFormat(artistsListened, s -> s))
                .setTopGenres(transformToFormat(genresListened, s -> s))
                .setTopAlbums(transformToFormat(albumsListened, s -> s))
                .setTopEpisodes(transformToFormat(listenedEpisodes,
                        AudioFile::getName))
                .build();
    }

    @Override
    public void addOneListen(final OneListen listen) {
        lastListen = listen;
        listen.getAudioFile().acceptListen(visitorAudioFile);
    }

}
