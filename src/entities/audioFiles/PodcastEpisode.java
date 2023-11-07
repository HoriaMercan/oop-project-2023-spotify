package entities.audioFiles;

public class PodcastEpisode extends AudioFile{
	protected String description;

	public PodcastEpisode(String name, Integer duration, String description) {
		super(name, duration);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
