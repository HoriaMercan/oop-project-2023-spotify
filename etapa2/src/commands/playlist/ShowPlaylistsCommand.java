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

public final class ShowPlaylistsCommand extends AbstractCommand {
    public ShowPlaylistsCommand(final ShowPlaylistsInput showPlaylistsInput) {
        super(showPlaylistsInput);
        this.commandOutput = new ShowPlaylistsOutput(showPlaylistsInput);
    }

    @Override
    public void executeCommand() {
        ShowPlaylistsInput input = (ShowPlaylistsInput) this.commandInput;
        ShowPlaylistsOutput output = (ShowPlaylistsOutput) this.commandOutput;

        List<Playlist> playlistsResult = MyDatabase.getInstance().getPublicPlaylists().stream().
                filter(playlist -> playlist.getOwner().equals(input.getUsername()))
                .toList();

        Function<Song, String> toSongName = AudioFile::getName;
        for (Playlist playlist : playlistsResult) {
            output.result.add(new ShowPlaylistsResults(
                    playlist.getName(), playlist.getSongs().stream().map(toSongName)
                    .collect(Collectors.toList()),
                    playlist.getVisibility(), playlist.followersNo()
            ));
        }
    }

    @Override
    public ShowPlaylistsOutput getCommandOutput() {
        return (ShowPlaylistsOutput) this.commandOutput;
    }

    public static final class ShowPlaylistsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new ShowPlaylistsCommand(this);
        }
    }

    public static final class ShowPlaylistsOutput extends AbstractCommand.CommandOutput {
        private final List<ShowPlaylistsResults> result = new ArrayList<>();
        @JsonIgnore
        private String message;

        public ShowPlaylistsOutput(final CommandInput commandInput) {
            super(commandInput);
        }

        public List<ShowPlaylistsResults> getResult() {
            return result;
        }

        public static final class ShowPlaylistsResults {
            private String name;
            private List<String> songs;

            private String visibility;
            private Integer followers;

            ShowPlaylistsResults() {
            }

            public ShowPlaylistsResults(final String name, final List<String> songs,
                                        final String visibility, final Integer followers) {
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
    }
}
