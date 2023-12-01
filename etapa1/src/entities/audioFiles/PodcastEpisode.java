package entities.audioFiles;

import fileio.input.EpisodeInput;

public final class PodcastEpisode extends AudioFile {

    private String description;

    public PodcastEpisode(final String name, final Integer duration, final String description) {
        super(name, duration);
        this.description = description;
    }

    public PodcastEpisode(final EpisodeInput input) {
        this(input.getName(), input.getDuration(), input.getDescription());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
