package gateways;

import entities.User.UserPlayer;
import entities.audioCollections.Podcast;
import entities.audioFiles.AudioFile;
import entities.helpers.Filter;
import databases.MyDatabase;
import entities.User;
import entities.audioCollections.Playlist;
import entities.audioFiles.Song;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class SearchBarAPI {
	private static final MyDatabase database;

	static {
		database = MyDatabase.getInstance();
	}

	// Method to check whether all elements of a list are included
	// in other
	private static <T> boolean isIncluded(List<T> list, List<T> sublist) {
		Set<T> set = new HashSet<T>(list);
		set.addAll(new ArrayList<>(sublist));
		return set.size() == list.size();
	}

	private static boolean isRelativeToReleaseYear(Integer releaseYear, String s) {
		if (s.length() < 2)
			return true;
		return switch (s.substring(0, 1)) {
			case "<" -> releaseYear < Integer.parseInt(s.substring(1));
			case ">" -> releaseYear > Integer.parseInt(s.substring(1));
			default -> false;
		};
	}

	public static List<String> getSongsByFilter(String username, Integer timestamp, Filter filter) {

		List<Song> songs = database.getSongs();
		List<String> resultSongs;

		Predicate<Song> byTag = song -> filter.getTags().isEmpty()
				|| isIncluded(song.getTags(), filter.getTags());
		Predicate<Song> byGenre = song ->
				song.getGenre().toLowerCase().startsWith(filter.getGenre().toLowerCase());
		Predicate<Song> byName = song -> song.getName().startsWith(filter.getName());
		Predicate<Song> byLyrics = song -> song.getLyrics().toLowerCase()
				.contains(filter.getLyrics().toLowerCase());
		Predicate<Song> byAlbum = song -> song.getAlbum().startsWith(filter.getAlbum());
		Predicate<Song> byArtist = song -> filter.getArtist().isEmpty() || song.getArtist().equalsIgnoreCase(filter.getArtist());
		Predicate<Song> byReleaseYear = song ->
				isRelativeToReleaseYear(song.getReleaseYear(), filter.getReleaseYear());
		Function<Song, String> songToName = AudioFile::getName;

		resultSongs = songs.stream().filter(byTag).filter(byGenre).filter(byName)
				.filter(byLyrics).filter(byAlbum).filter(byArtist)
				.filter(byReleaseYear)
				.map(songToName).toList()
				.stream().limit(5).toList();

		User user = database.findUserByUsername(username);
		if (user != null) {
			user.getPlayer().setLastSearched(resultSongs);
			user.getPlayer().setTypeSearched("song");
			user.getPlayer().setLastSelected("");
			user.getPlayer().unsetContext(timestamp);
		}
		return resultSongs;
	}

	public static List<String> getPlaylistsByFilter(String username, Integer timestamp, Filter filter) {
		List<String> resultPlaylists = new ArrayList<>();
		User user = database.findUserByUsername(username);
		if (user == null)
			return resultPlaylists;

		List<Playlist> playlists = database.getPublicPlaylists();

		Predicate<Playlist> byPermission = playlist -> playlist.isPublic() || playlist.getOwner().equals(username);
		Predicate<Playlist> byName = playlist -> playlist.getName().contains(filter.getName());
		Predicate<Playlist> byOwner = playlist -> filter.getOwner().isEmpty() ||
				playlist.getOwner().equals(filter.getOwner());

		Function<Playlist, String> playlistToName = Playlist::getName;

		resultPlaylists = playlists.stream().filter(byPermission)
				.filter(byName).filter(byOwner)
				.map(playlistToName).toList()
				.stream().limit(5).toList();

		user.getPlayer().setLastSearched(resultPlaylists);
		user.getPlayer().setTypeSearched("playlist");
		user.getPlayer().setLastSelected("");
		user.getPlayer().unsetContext(timestamp);
		return resultPlaylists;
	}

	public static List<String> getPodcastsByFilter(String username, Integer timestamp, Filter filter) {
		List<String> resultPodcasts;

		List<Podcast> podcasts = database.getPodcasts();

		Predicate<Podcast> byName = podcast -> podcast.getName().startsWith(filter.getName());
		Predicate<Podcast> byOwner = podcast -> podcast.getOwner().startsWith(filter.getOwner());

		Function<Podcast, String> podcastToName = Podcast::getName;
		resultPodcasts = podcasts.stream().filter(byName).filter(byOwner)
				.map(podcastToName).toList()
				.stream().limit(5).toList();

		User user = database.findUserByUsername(username);
		if (user != null) {
			user.getPlayer().setLastSearched(resultPodcasts);
			user.getPlayer().setTypeSearched("podcast");
			user.getPlayer().setLastSelected("");
			user.getPlayer().unsetContext(timestamp);
		}
		return resultPodcasts;
	}

	// make the selection and get the message
	public static String getSelectionMessage(String username, Integer itemNumber) {
		UserPlayer player = database.findUserByUsername(username).getPlayer();
		List<String> lastSearched = player.getLastSearched();
		if (lastSearched == null) {
			return "Please conduct a search before making a selection.";
		}
		if (lastSearched.size() < itemNumber) {
			return "The selected ID is too high.";
		}


		player.setLastSelected(lastSearched.get(itemNumber - 1));
//		System.out.println("Last select:" + lastSearched.get(itemNumber - 1));
		return "Successfully selected " + player.getLastSelected() + ".";
	}
}
