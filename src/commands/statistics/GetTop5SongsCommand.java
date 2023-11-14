package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import commands.statistics.GetTop5PlaylistsCommand.GetTop5PlaylistsOutput;
import databases.MyDatabase;
import entities.audioCollections.AudioCollection;
import entities.audioCollections.Playlist;
import entities.audioFiles.AudioFile;
import entities.audioFiles.Song;

import java.util.Comparator;
import java.util.List;

public class GetTop5SongsCommand extends AbstractCommand {
	public GetTop5SongsCommand(GetTop5SongsInput getTop5SongsInput) {
		super(getTop5SongsInput);
		this.commandOutput = new GetTop5SongsOutput(getTop5SongsInput);
	}

	public static class GetTop5SongsInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new GetTop5SongsCommand(this);
		}
	}

	public static class GetTop5SongsOutput extends AbstractCommand.CommandOutput {
		@JsonIgnore
		protected String user;
		@JsonIgnore
		protected String message;
		List<String> result;
		public GetTop5SongsOutput(CommandInput commandInput) {
			super(commandInput);
		}
		public List<String> getResult() {
			return result;
		}

		public void setResult(List<String> result) {
			this.result = result;
		}
	}

	@Override
	public GetTop5SongsOutput getCommandOutput() {
		return (GetTop5SongsOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		GetTop5SongsOutput output = (GetTop5SongsOutput) commandOutput;

		List<Song>songs = new java.util.ArrayList<>(MyDatabase.getInstance()
				.getSongs());

		songs.sort(new Comparator<Song>() {
			@Override
			public int compare(Song song, Song t1) {
				if (song.likesNo() > t1.likesNo()) {
					return -1;
				}
				if (song.likesNo().equals(t1.likesNo()))
					return 0;
				return 1;
			}
		});

		output.result = songs.stream().map(AudioFile::getName).limit(5)
				.toList();
	}
}
