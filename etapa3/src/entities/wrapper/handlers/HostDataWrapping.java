package entities.wrapper.handlers;

import lombok.Getter;

import java.util.List;
import java.util.Map;


public final class HostDataWrapping extends AbstractDataWrapping {
    @Getter
    private final Map<String, Integer> topEpisodes;
    private final List<String> topFans;

    @Getter
    private final Integer listeners;

    private HostDataWrapping(final Map<String, Integer> topEpisodes,
                             final List<String> topFans, final Integer listeners) {
        this.topEpisodes = topEpisodes;
        this.topFans = topFans;
        this.listeners = listeners;
    }

    private HostDataWrapping(final HostDataWrapping.Builder builder) {
        this(builder.getTopEpisodes(), builder.getTopFans(), builder.getListeners());
    }

    public static final class Builder extends AbstractBuilder {
        @Override
        public HostDataWrapping build() {
            return new HostDataWrapping(this);
        }
    }

    public static final Builder BUILDER = new Builder();
}
