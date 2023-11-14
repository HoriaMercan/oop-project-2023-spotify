package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioFiles.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShowPreferredSongsCommand extends AbstractCommand {
	public ShowPreferredSongsCommand(ShowPreferredSongsInput showPreferredSongsInput) {
		super(showPreferredSongsInput);
		this.commandOutput = new ShowPreferredSongsOutput(showPreferredSongsInput);
	}

	public static class ShowPreferredSongsInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new ShowPreferredSongsCommand(this);
		}
	}

	public static class ShowPreferredSongsOutput extends AbstractCommand.CommandOutput {
		@JsonIgnore
		protected String message;
		List<String> result = new ArrayList<>();

		public List<String> getResult() {
			return result;
		}

		public ShowPreferredSongsOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}

	@Override
	public ShowPreferredSongsOutput getCommandOutput() {
		return (ShowPreferredSongsOutput) commandOutput;
	}

	private class SongTimestamp implements Comparable {
		protected String songName;
		protected Integer timestamp;
		SongTimestamp(String songName, Integer timestamp) {
			this.songName = songName;
			this.timestamp = timestamp;
		}

		public String getSongName() {
			return songName;
		}

		@Override
		public int compareTo(Object o) {
			SongTimestamp st = (SongTimestamp) o;
			if (this.timestamp < st.timestamp)
				return -1;
			if (this.timestamp.equals(st.timestamp))
				return this.songName.compareTo(st.songName);
			return 1;
		}
	}
	@Override
	public void executeCommand() {
		ShowPreferredSongsInput input = (ShowPreferredSongsInput) commandInput;
		ShowPreferredSongsOutput output = (ShowPreferredSongsOutput) commandOutput;

		List<Song> songs = MyDatabase.getInstance().getSongs();

		ArrayList<SongTimestamp> stList = new ArrayList<>();
		for (Song song : songs) {
			if (song.isSongLikedByUser(commandInput.getUsername())) {
				stList.add(new SongTimestamp(song.getName(),
						song.getTimestampOfLike(input.getUsername())));
			}
		}

		output.result = stList.stream().sorted().map(SongTimestamp::getSongName).toList();

	}
}
