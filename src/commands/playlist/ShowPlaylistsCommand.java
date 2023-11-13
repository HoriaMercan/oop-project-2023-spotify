package commands.playlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import commands.playlist.ShowPlaylistsCommand.ShowPlaylistsOutput.ShowPlaylistsResults;
import databases.MyDatabase;
import entities.audioCollections.Playlist;
import entities.audioFiles.AudioFile;
import entities.audioFiles.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShowPlaylistsCommand extends AbstractCommand {
	public ShowPlaylistsCommand(ShowPlaylistsInput showPlaylistsInput) {
		super(showPlaylistsInput);
		this.commandOutput = new ShowPlaylistsOutput(showPlaylistsInput);
	}

	public static class ShowPlaylistsInput extends AbstractCommand.CommandInput {
		public Integer playlistId;
		@Override
		public AbstractCommand getCommandFromInput() {
			return new ShowPlaylistsCommand(this);
		}
	}


	public static class ShowPlaylistsOutput extends AbstractCommand.CommandOutput {
		@JsonIgnore
		protected String message;

		public static class ShowPlaylistsResults {
			String name;
			List<String> songs;

			String visibility;
			Integer followers;

			ShowPlaylistsResults() {}
			public ShowPlaylistsResults(String name, List<String> songs, String visibility, Integer followers) {
				this.name = name;
				this.songs = songs;
				this.visibility = visibility;
				this.followers = followers;
			}

			public String getName() {
				return name;
			}

			public List<String> getSongs() {
				return songs;
			}

			public String getVisibility() {
				return visibility;
			}

			public Integer getFollowers() {
				return followers;
			}
		}
		List<ShowPlaylistsResults> result = new ArrayList<>();

		public List<ShowPlaylistsResults> getResult() {
			return result;
		}

		public ShowPlaylistsOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}

	@Override
	public ShowPlaylistsOutput getCommandOutput() {
		return (ShowPlaylistsOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		ShowPlaylistsInput input = (ShowPlaylistsInput) this.commandInput;
		ShowPlaylistsOutput output = (ShowPlaylistsOutput) this.commandOutput;

		List<Playlist>playlistsResult = MyDatabase.getInstance().getPublicPlaylists().stream().
				filter(playlist -> playlist.getOwner().equals(input.getUsername()))
				.toList();

		Function<Song, String> toSongName = AudioFile::getName;
		for (Playlist playlist: playlistsResult) {
			output.result.add(new ShowPlaylistsResults(
					playlist.getName(), playlist.getSongs().stream().map(toSongName).collect(Collectors.toList()),
					playlist.getVisibility(), playlist.getFollowersNumber()
			));
		}
	}
}
