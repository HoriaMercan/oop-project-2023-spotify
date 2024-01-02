package entities.wrapper.handlers;


import lombok.Getter;

import java.util.Map;

/**
 * Implements builder pattern that will be used by every other class (where it is needed)
 * <p>
 * This class will be used in the context of a Factory Method Pattern for the command
 * of building a data wrapper for each user
 */
public abstract class AbstractDataWrapping {

    @Getter
    public static abstract class AbstractBuilder {
        protected Map<String, Integer> topArtists = null;
        protected Map<String, Integer> topGenres = null;
        protected Map<String, Integer> topSongs = null;
        protected Map<String, Integer> topAlbums = null;
        protected Map<String, Integer> topEpisodes = null;
        protected Map<String, Integer> topFans = null;
        protected Integer listeners = null;

        public AbstractBuilder setTopArtists(Map<String, Integer> topArtists) {
            this.topArtists = topArtists;
            return this;
        }

        public AbstractBuilder setTopGenres(Map<String, Integer> topGenres) {
            this.topGenres = topGenres;
            return this;
        }

        public AbstractBuilder setTopSongs(Map<String, Integer> topSongs) {
            this.topSongs = topSongs;
            return this;
        }

        public AbstractBuilder setTopAlbums(Map<String, Integer> topAlbums) {
            this.topAlbums = topAlbums;
            return this;
        }

        public AbstractBuilder setTopEpisodes(Map<String, Integer> topEpisodes) {
            this.topEpisodes = topEpisodes;
            return this;
        }

        public AbstractBuilder setTopFans(Map<String, Integer> topFans) {
            this.topFans = topFans;
            return this;
        }

        public AbstractBuilder setListeners(Integer listeners) {
            this.listeners = listeners;
            return this;
        }

        public AbstractDataWrapping build() {return null;}
    }
}
