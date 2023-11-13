package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioFiles.Song;

import java.util.ArrayList;
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

	@Override
	public void executeCommand() {
		ShowPreferredSongsInput input = (ShowPreferredSongsInput) commandInput;
		ShowPreferredSongsOutput output = (ShowPreferredSongsOutput) commandOutput;

		List<Song> songs = MyDatabase.getInstance().getSongs();

		for (Song song : songs) {
			if (song.isSongLikedByUser(commandInput.getUsername()))
				output.result.add(song.getName());
		}
	}
}
