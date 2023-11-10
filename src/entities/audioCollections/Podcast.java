package entities.audioCollections;

import entities.audioFiles.PodcastEpisode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;

import java.util.ArrayList;
import java.util.List;

public class Podcast extends AudioCollection {
	private List<PodcastEpisode> episodes;
	public Podcast() {
	}

	public Podcast(String name, String owner, List<PodcastEpisode> episodes) {
		this.name = name;
		this.owner = owner;
		this.episodes = episodes;
	}

	public Podcast(PodcastInput input) {
		ArrayList<PodcastEpisode>episodes = new ArrayList<PodcastEpisode>();

		for (EpisodeInput every: input.getEpisodes()) {
			episodes.add(new PodcastEpisode(every));
		}
		this.name = input.getName();
		this.owner = input.getOwner();
		this.episodes = episodes;
	}

	public List<PodcastEpisode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(final List<PodcastEpisode> episodes) {
		this.episodes = episodes;
	}

}
