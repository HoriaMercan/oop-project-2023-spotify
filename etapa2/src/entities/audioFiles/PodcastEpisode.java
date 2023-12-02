package entities.audioFiles;

import fileio.input.EpisodeInput;
import lombok.Getter;

@Getter
public final class PodcastEpisode extends AudioFile {

    private String description;

    public PodcastEpisode(){}
    public PodcastEpisode(final String name, final Integer duration, final String description) {
        super(name, duration);
        this.description = description;
    }

    public PodcastEpisode(final EpisodeInput input) {
        this(input.getName(), input.getDuration(), input.getDescription());
    }

    public PodcastEpisode(final EpisodeInput input, final String creator) {
        this(input.getName(), input.getDuration(), input.getDescription());
        this.creator = creator;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
