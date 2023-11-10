package entities.audioFiles;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;

public class PodcastEpisode extends AudioFile{

	protected String description;

	public PodcastEpisode(String name, Integer duration, String description) {
		super(name, duration);
		this.description = description;
	}
	public PodcastEpisode(EpisodeInput input) {
		this(input.getName(), input.getDuration(), input.getDescription());
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
