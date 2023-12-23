package entities.audioCollections;

import entities.audioFiles.PodcastEpisode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
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

    public Podcast(final PodcastInput input, final String creator) {
        this.episodes = new ArrayList<>();

        for (EpisodeInput every : input.getEpisodes()) {
            this.episodes.add(new PodcastEpisode(every, creator));
        }
        this.name = input.getName();
        this.owner = input.getOwner();
    }

    public void setEpisodes(final List<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + ":\n\t[");

        if (!episodes.isEmpty()) {
            for (PodcastEpisode ep : episodes) {
                sb.append(ep).append(", ");
            }

            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]\n");
        return sb.toString();
    }

    @Override
    public List<PodcastEpisode> getAudioFiles() {
        return episodes;
    }
}
