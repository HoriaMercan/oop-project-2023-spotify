package entities.audioColections;

import entities.audioFiles.PodcastEpisode;

import java.util.ArrayList;
import java.util.List;

public class Podcast {
	private String name;
	private String owner;
	private List<PodcastEpisode> episodes;

	public Podcast() {
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(final String owner) {
		this.owner = owner;
	}

	public List<PodcastEpisode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(final List<PodcastEpisode> episodes) {
		this.episodes = episodes;
	}

}
