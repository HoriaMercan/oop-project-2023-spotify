package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.AudioCollection;
import entities.audioCollections.Playlist;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetTop5PlaylistsCommand extends AbstractCommand {
	public GetTop5PlaylistsCommand(GetTop5PlaylistsInput getTop5PlaylistsInput) {
		super(getTop5PlaylistsInput);
		this.commandOutput = new GetTop5PlaylistsOutput(getTop5PlaylistsInput);
	}

	public static class GetTop5PlaylistsInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new GetTop5PlaylistsCommand(this);
		}
	}

	public static class GetTop5PlaylistsOutput extends AbstractCommand.CommandOutput {
		@JsonIgnore
		protected String user;
		@JsonIgnore
		protected String message;

		List<String> result;
		public GetTop5PlaylistsOutput(CommandInput commandInput) {
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
	public GetTop5PlaylistsOutput getCommandOutput() {
		return (GetTop5PlaylistsOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		GetTop5PlaylistsOutput output = (GetTop5PlaylistsOutput) commandOutput;

		List<Playlist>publicPlaylists = new java.util.ArrayList<>(MyDatabase.getInstance()
				.getPublicPlaylists().stream().filter(Playlist::isPublic).toList());

		publicPlaylists.sort(new Comparator<Playlist>() {
			@Override
			public int compare(Playlist playlist, Playlist t1) {
				if (playlist.followersNo() > t1.followersNo())
					return -1;
				if (playlist.followersNo().equals(t1.followersNo()))
					return 0;
				return 1;
			}
		});

		output.result = publicPlaylists.stream().map(AudioCollection::getName).limit(5)
						.toList();
	}
}
