package entities.audioColections;

import databases.StaticDatabase;
import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;

import java.util.List;

public class Library {
	private final List<Song>songs;
	private final List<PodcastEpisode>podcastEpisodes;
	public Library() {
		songs = StaticDatabase.getInstance().getSongs();
		podcastEpisodes = StaticDatabase.getInstance().getPodcastEpisodes();
	}
}
