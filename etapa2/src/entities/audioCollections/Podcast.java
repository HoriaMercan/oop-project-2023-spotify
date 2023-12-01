package entities.audioCollections;

import entities.audioFiles.PodcastEpisode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;

import java.util.ArrayList;
import java.util.List;

public final class Podcast extends AudioCollection {
    private List<PodcastEpisode> episodes;

    public Podcast() {
    }

    public Podcast(final String name, final String owner, final List<PodcastEpisode> episodes) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
    }

    public Podcast(final PodcastInput input) {
        this.episodes = new ArrayList<>();

        for (EpisodeInput every : input.getEpisodes()) {
            this.episodes.add(new PodcastEpisode(every));
        }
        this.name = input.getName();
        this.owner = input.getOwner();
    }

    public List<PodcastEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(final List<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + ":\\n\\t[");
        for (PodcastEpisode ep: episodes) {
            sb.append(ep).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }

}
