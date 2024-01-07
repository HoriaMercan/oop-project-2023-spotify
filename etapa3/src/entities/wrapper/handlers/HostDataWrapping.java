package entities.wrapper.handlers;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public final class HostDataWrapping extends AbstractDataWrapping {
    private final Map<String, Integer> topPodcasts;
    private final List<String> topFans;
    private final Integer listeners;

    private HostDataWrapping(final Map<String, Integer> topEpisodes,
                             final List<String> topFans, final Integer listeners) {
        this.topPodcasts = topEpisodes;
        this.topFans = topFans;
        this.listeners = listeners;
    }

    private HostDataWrapping(HostDataWrapping.Builder builder) {
        this(builder.getTopEpisodes(), builder.getTopFans(), builder.getListeners());
    }

    public static final class Builder extends AbstractBuilder {
        @Override
        public HostDataWrapping build() {
            return new HostDataWrapping(this);
        }
    }

    public static final Builder builder = new Builder();
}
