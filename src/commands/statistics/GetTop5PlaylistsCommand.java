package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.AudioCollection;
import entities.audioCollections.Playlist;

import java.util.Comparator;
import java.util.List;

public final class GetTop5PlaylistsCommand extends AbstractCommand {
    public GetTop5PlaylistsCommand(final GetTop5PlaylistsInput getTop5PlaylistsInput) {
        super(getTop5PlaylistsInput);
        this.commandOutput = new GetTop5PlaylistsOutput(getTop5PlaylistsInput);
    }

    @Override
    public void executeCommand() {
        GetTop5PlaylistsOutput output = (GetTop5PlaylistsOutput) commandOutput;

        List<Playlist> publicPlaylists = new java.util.ArrayList<>(MyDatabase.getInstance()
                .getPublicPlaylists().stream().filter(Playlist::isPublic).toList());

        publicPlaylists.sort(new Comparator<Playlist>() {
            @Override
            public int compare(final Playlist playlist, final Playlist t1) {
                if (playlist.followersNo() > t1.followersNo()) {
                    return -1;
                }
                if (playlist.followersNo().equals(t1.followersNo())) {
                    return 0;
                }
                return 1;
            }
        });
        final int limit = 5;
        output.result = publicPlaylists.stream().map(AudioCollection::getName).limit(limit)
                .toList();
    }

    @Override
    public GetTop5PlaylistsOutput getCommandOutput() {
        return (GetTop5PlaylistsOutput) this.commandOutput;
    }

    public static final class GetTop5PlaylistsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new GetTop5PlaylistsCommand(this);
        }
    }

    public static final class GetTop5PlaylistsOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        protected String user;
        @JsonIgnore
        protected String message;
        private List<String> result;

        public GetTop5PlaylistsOutput(final CommandInput commandInput) {
            super(commandInput);
        }

        public List<String> getResult() {
            return result;
        }
        public void setResult(final List<String> result) {
            this.result = result;
        }
    }
}
