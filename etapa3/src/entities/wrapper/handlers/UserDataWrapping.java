package entities.wrapper.handlers;

import lombok.Getter;

import java.util.Map;

@Getter
public final class UserDataWrapping extends AbstractDataWrapping {
    private final Map<String, Integer> topArtists;
    private final Map<String, Integer> topGenres;
    private final Map<String, Integer> topSongs;
    private final Map<String, Integer> topAlbums;
    private final Map<String, Integer> topPodcasts;

    private UserDataWrapping(Map<String, Integer> topArtists, Map<String, Integer> topGenres,
                             Map<String, Integer> topSongs, Map<String, Integer> topAlbums,
                             Map<String, Integer> topEpisodes) {
        this.topArtists = topArtists;
        this.topGenres = topGenres;
        this.topSongs = topSongs;
        this.topAlbums = topAlbums;
        this.topPodcasts = topEpisodes;
    }

    private UserDataWrapping(UserDataWrapping.Builder builder) {
        this(builder.getTopArtists(), builder.getTopGenres(), builder.getTopSongs(),
                builder.getTopAlbums(), builder.getTopEpisodes());
    }

    public static final class Builder extends AbstractBuilder {
        @Override
        public UserDataWrapping build() {
            return new UserDataWrapping(this);
        }
    }

    public static final Builder builder = new Builder();
}
