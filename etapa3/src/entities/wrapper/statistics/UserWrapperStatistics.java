package entities.wrapper.statistics;

import databases.MyDatabase;
import entities.audioCollections.Album;
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
import gateways.AdminAPI;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserWrapperStatistics extends WrapperStatistics {

    private final Map<Song, Integer> listenedSongs = new HashMap<>();
    private final Map<PodcastEpisode, Integer> listenedEpisodes = new HashMap<>();
    private final Map<String, Integer> artistsListened = new HashMap<>();
    private final Map<String, Integer> genresListened = new HashMap<>();
    private final Map<String, Integer> albumsListened = new HashMap<>();
    private OneListen lastListen = null;
    private final VisitorWrapper visitorAudioFile = new VisitorWrapper() {
        @Override
        public void visitListen(User user) {

        }

        @Override
        public void visitListen(Song song) {
            listenedSongs.compute(song, updater);
            String artistName = song.getArtist();
            String genre = song.getGenre();
            String albumName = song.getAlbum();

            if (artistName != null) {
                artistsListened.compute(artistName, stringUpdater);
            }
            if (genre != null) {
                genresListened.compute(genre, stringUpdater);
            }

            if (albumName != null) {
                albumsListened.compute(albumName, stringUpdater);
            }
            // ToDo: add to artists statistics

            Artist artist = MyDatabase.getInstance().findArtistByUsername(artistName);
            if (artist == null) {
                return;
            }

            artist.getWrapperStatistics().addOneListen(lastListen);

        }

        @Override
        public void visitListen(PodcastEpisode podcastEpisode) {
            listenedEpisodes.compute(podcastEpisode, updater);

            // ToDo: add to host statistics
            Host host = MyDatabase.getInstance()
                    .findHostByUsername(podcastEpisode.getCreator());

            if (host == null) {
                return;
            }

            host.getWrapperStatistics().addOneListen(lastListen);
        }
    };


    public AbstractDataWrapping getDataWrapping() {

        return UserDataWrapping.builder
                .setTopSongs(transformToFormat(listenedSongs, AudioFile::getName))
                .setTopArtists(transformToFormat(artistsListened, s->s))
                .setTopGenres(transformToFormat(genresListened, s->s))
                .setTopAlbums(transformToFormat(albumsListened, s->s))
                .setTopEpisodes(transformToFormat(listenedEpisodes,
                        AudioFile::getName))
                .build();
    }

    public void addOneListen(OneListen listen) {
        lastListen = listen;
        listen.getAudioFile().acceptListen(visitorAudioFile);
    }

}
