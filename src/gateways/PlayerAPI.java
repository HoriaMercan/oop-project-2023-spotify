package gateways;

import commands.player.StatusCommand.StatusOutput.Stats;
import databases.MyDatabase;
import entities.User;
import entities.User.UserPlayer;
import entities.audioCollections.Playlist;
import entities.audioCollections.Podcast;
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
		UserPlayer player = user.getPlayer();

		if (player.getLastSelected().isEmpty()) {
			return "Please select a source before attempting to load.";
		}
		switch (player.getTypeSearched()) {
			case "song":
				player.setContext(getSongListFromStrings(player.getLastSelected()), timestamp);
				break;

			case "playlist":
				Playlist playlist = database.findPlaylistByName(player.getLastSelected());
				if (playlist.getSongs().isEmpty()) {
					return "You can't load an empty audio collection!";
				}
				player.setContext(getSongListFromPlaylist(playlist), timestamp);
				break;

			case "podcast":
				Podcast podcast = database.findPodcastByName(player.getLastSelected());
				if (podcast.getEpisodes().isEmpty()) {
					return "You can't load an empty audio collection!";
				}
				player.setContext(getEpisodesFromPodcast(podcast), timestamp);
				player.setPlayedPodcastName(podcast.getName());
				break;
			default:
				break;
		}

		player.loadPlayer(timestamp);
		player.setLastSelected("");
		player.setLastSearched(null);
		player.setTypeSearched("");
		return "Playback loaded successfully.";
	}

	public static void setStatus(Stats stats, String username, Integer timestamp) {
		User user = database.findUserByUsername(username);
		UserPlayer player = user.getPlayer();
		player.updatePlayer(timestamp);

		if (player.getTypeLoaded().isEmpty()) {
			stats.setName("");
			stats.setRemainedTime(0);
			stats.setPaused(true);
			stats.setShuffle(false);
			stats.setRepeat("No Repeat");
		} else {
			stats.setName(player.getCurrentPlayed().getName());
			stats.setRemainedTime(player.getRemainedTime());
			stats.setPaused(player.isPaused());
			stats.setShuffle(player.isShuffle());
			stats.setRepeat(player.getRepeatStatus());
		}


	}

	public static String getPlayPauseMessage(String username, Integer timestamp) {
		User user = database.findUserByUsername(username);
		user.getPlayer().updatePlayer(timestamp);
		if (user.getPlayer().getTypeLoaded().isEmpty()) {
			return "Please load a source before attempting to pause or resume playback.";
		}
		return user.getPlayer().playPause(timestamp);
	}

	public static String getCreatePlaylistCommand(String username, String playlistName) {
		User user = database.findUserByUsername(username);

		Playlist playlist = database.findPlaylistByName(playlistName);
		if (playlist != null) {
			return "A playlist with the same name already exists.";
		}

		playlist = new Playlist(username, true, new ArrayList<Song>());
		playlist.setName(playlistName);
		database.addPlaylistInDatabase(playlist);
		user.addPlaylistInUserList(playlistName);
		return "Playlist created successfully.";
	}

	public static String getCurrentPlayedType(String username, Integer timestamp) {
		User user = database.findUserByUsername(username);
		user.getPlayer().updatePlayer(timestamp);
		return user.getPlayer().getTypeLoaded();
	}

	public static String getAddRemoveMessage(String username, Integer timestamp, Integer playlistID) {
		User user = database.findUserByUsername(username);
		user.getPlayer().updatePlayer(timestamp);
		if (user.getPlayer().getTypeLoaded().isEmpty()) {
			return "Please load a source before adding to or removing from the playlist.";
		}
		if (!user.getPlayer().getTypeLoaded().equals("song")) {
			return "The loaded source is not a song.";
		}
		if (!user.isPlaylistIDInUserList(playlistID)) {
			return "The specified playlist does not exist.";
		}

		Song song = (Song) user.getPlayer().getCurrentPlayed();
		Playlist playlist = database.findPlaylistByName(user.getPlaylistFromID(playlistID));
		if (playlist.removeSongFromPlaylist(song)) {
			return "Successfully removed from playlist.";
		}
		playlist.addSongInPlaylist(song);
		return "Successfully added to playlist.";
	}

	public static String getLikeMessage(String username, Integer timestamp) {
		User user = database.findUserByUsername(username);
		String currentType = getCurrentPlayedType(username, timestamp);
		if (currentType.equals("song") || currentType.equals("playlist")) {
			Song song = (Song) user.getPlayer().getCurrentPlayed();
			if (song.songUnlikeByUser(username)) {
				return "Unlike registered successfully.";
			}
			song.songLikeByUser(username, timestamp);
			return "Like registered successfully.";
		} else if (currentType.isEmpty()) {
			return "Please load a source before liking or unliking.";
		}
		return "Loaded source is not a song.";
	}
}
