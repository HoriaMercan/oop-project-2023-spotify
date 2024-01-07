package entities.wrapper.handlers;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public final class ArtistDataWrapping extends AbstractDataWrapping {
    private final Map<String, Integer> topAlbums;
    private final Map<String, Integer> topSongs;
    private final List<String> topFans;
    private final Integer listeners;

    private ArtistDataWrapping(Map<String, Integer> topSongs, Map<String, Integer> topAlbums,
                             List<String> topFans, final Integer listeners) {
        this.topSongs = topSongs;
        this.topAlbums = topAlbums;
        this.topFans = topFans;
        this.listeners = listeners;
    }

    private ArtistDataWrapping(ArtistDataWrapping.Builder builder) {
        this(builder.getTopSongs(),
                builder.getTopAlbums(), builder.getTopFans(), builder.getListeners());
    }

    public static final class Builder extends AbstractBuilder {
        @Override
        public ArtistDataWrapping build() {
            return new ArtistDataWrapping(this);
        }
    }

    public static final Builder builder = new Builder();
}
