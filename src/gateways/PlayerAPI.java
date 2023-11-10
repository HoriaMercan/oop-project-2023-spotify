package gateways;

import commands.player.StatusCommand.StatusOutput.Stats;
import databases.MyDatabase;
import entities.User;
import entities.User.UserPlayer1;
import entities.audioCollections.Playlist;
import entities.audioCollections.Podcast;
import entities.audioFiles.AudioFile;
import entities.audioFiles.PodcastEpisode;
import entities.audioFiles.Song;

import java.util.ArrayList;
import java.util.List;

public class PlayerAPI {
	private static final MyDatabase database;

	static {
		database = MyDatabase.getInstance();
	}

	private static List<Song> getSongListFromStrings(String s) {
		List<Song> list = new ArrayList<Song>();
		list.add(database.findSongByName(s));
		return list;
	}

	private static List<Song> getSongListFromPlaylist(Playlist p) {
		return new ArrayList<>(p.getSongs());
	}

	private static List<PodcastEpisode> getEpisodesFromPodcast(Podcast p) {
		return new ArrayList<>(p.getEpisodes());
	}
	public static String getLoadMessage(String username, Integer timestamp) {
		User user = database.findUserByUsername(username);
		UserPlayer1 player = user.getPlayer();

		if (player.getLastSelected().isEmpty()) {
			return "Please select a source before attempting to load.";
		}
		switch(player.getTypeSearched()) {
			case "song":
				player.setContext(getSongListFromStrings(player.getLastSelected()));
				break;

			case "playlist":
				Playlist playlist = database.findPlaylistByName(player.getLastSelected());
				if (playlist.getSongs().isEmpty()) {
					return "You can't load an empty audio collection!";
				}
				player.setContext(getSongListFromPlaylist(playlist));
				break;

			case "podcast":
				Podcast podcast = database.findPodcastByName(player.getLastSelected());
				if (podcast.getEpisodes().isEmpty()) {
					return "You can't load an empty audio collection!";
				}
				player.setContext(getEpisodesFromPodcast(podcast));
				break;
			default:
				break;
		}

		player.loadPlayer(timestamp);
		return "Playback loaded successfully.";
	}

	public static void setStatus(Stats stats, String username, Integer timestamp) {
		User user = database.findUserByUsername(username);
		UserPlayer1 player = user.getPlayer();
		player.updatePlayer(timestamp);

		if (player.getTypeLoaded().isEmpty()) {
			stats.setName("");
		}
		else {
			stats.setName(player.getCurrentPlayed().getName());
		}

		stats.setRemainedTime(player.getRemainedTime());
		stats.setPaused(player.isPaused());
		stats.setShuffle(player.isShuffle());
		stats.setRepeat(player.getRepeatStatus());
	}

	public static String getPlayPauseMessage(String username, Integer timestamp) {
		User user = database.findUserByUsername(username);
		user.getPlayer().updatePlayer(timestamp);
		if (user.getPlayer().getTypeLoaded().isEmpty()) {
			return "Please load a source before attempting to pause or resume playback.";
		}
		return user.getPlayer().playPause(timestamp);
	}
}
