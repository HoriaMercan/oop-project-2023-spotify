package entities.wrapper.handlers;


import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Implements builder pattern that will be used by every other class (where it is needed)
 * <p>
 * This class will be used in the context of a Factory Method Pattern for the command
 * of building a data wrapper for each user
 */
public abstract class AbstractDataWrapping {

    @Getter
    public abstract static class AbstractBuilder {
        protected Map<String, Integer> topArtists = null;
        protected Map<String, Integer> topGenres = null;
        protected Map<String, Integer> topSongs = null;
        protected Map<String, Integer> topAlbums = null;
        protected Map<String, Integer> topEpisodes = null;
        protected List<String> topFans = null;
        protected Integer listeners = null;

        /**
         * @param topArtistsBuilder
         * @return
         */
        public final AbstractBuilder setTopArtists(final Map<String, Integer> topArtistsBuilder) {
            this.topArtists = topArtistsBuilder;
            return this;
        }

        /**
         * @param topGenresBuilder
         * @return
         */
        public final AbstractBuilder setTopGenres(final Map<String, Integer> topGenresBuilder) {
            this.topGenres = topGenresBuilder;
            return this;
        }

        /**
         * @param topSongsBuilder
         * @return
         */
        public final AbstractBuilder setTopSongs(final Map<String, Integer> topSongsBuilder) {
            this.topSongs = topSongsBuilder;
            return this;
        }

        /**
         * @param topAlbumsBuilder
         * @return
         */
        public final AbstractBuilder setTopAlbums(final Map<String, Integer> topAlbumsBuilder) {
            this.topAlbums = topAlbumsBuilder;
            return this;
        }

        /**
         * @param topEpisodesBuilder
         * @return
         */
        public final AbstractBuilder setTopEpisodes(final Map<String, Integer> topEpisodesBuilder) {
            this.topEpisodes = topEpisodesBuilder;
            return this;
        }

        /**
         * @param topFansBuilder
         * @return
         */
        public final AbstractBuilder setTopFans(final List<String> topFansBuilder) {
            this.topFans = topFansBuilder;
            return this;
        }

        /**
         * @param listenersBuilder
         * @return
         */
        public final AbstractBuilder setListeners(final Integer listenersBuilder) {
            this.listeners = listenersBuilder;
            return this;
        }

        /**
         * @return This method will be implemented in each Data Wrapping class for building
         * the wrapped output command
         */
        public abstract AbstractDataWrapping build();
    }
}
