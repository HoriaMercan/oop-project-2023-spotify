package entities.wrapper.handlers;

import lombok.Getter;

import java.util.Map;

@Getter
public final class UserDataWrapping extends AbstractDataWrapping {
    public static final Builder BUILDER = new Builder();
    private final Map<String, Integer> topArtists;
    private final Map<String, Integer> topGenres;
    private final Map<String, Integer> topSongs;
    private final Map<String, Integer> topAlbums;
    private final Map<String, Integer> topEpisodes;

    private UserDataWrapping(final Map<String, Integer> topArtists,
                             final Map<String, Integer> topGenres,
                             final Map<String, Integer> topSongs,
                             final Map<String, Integer> topAlbums,
                             final Map<String, Integer> topEpisodes) {
        this.topArtists = topArtists;
        this.topGenres = topGenres;
        this.topSongs = topSongs;
        this.topAlbums = topAlbums;
        this.topEpisodes = topEpisodes;
    }

    private UserDataWrapping(final UserDataWrapping.Builder builder) {
        this(builder.getTopArtists(), builder.getTopGenres(), builder.getTopSongs(),
                builder.getTopAlbums(), builder.getTopEpisodes());
    }

    public static final class Builder extends AbstractBuilder {
        @Override
        public UserDataWrapping build() {
            return new UserDataWrapping(this);
        }
    }
}
