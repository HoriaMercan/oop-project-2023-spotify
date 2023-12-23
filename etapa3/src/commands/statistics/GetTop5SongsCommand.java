package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioFiles.AudioFile;
import entities.audioFiles.Song;
import java.util.List;

public final class GetTop5SongsCommand extends AbstractCommand {
    public GetTop5SongsCommand(final GetTop5SongsInput getTop5SongsInput) {
        super(getTop5SongsInput);
        this.commandOutput = new GetTop5SongsOutput(getTop5SongsInput);
    }

    @Override
    public void executeCommand() {
        GetTop5SongsOutput output = (GetTop5SongsOutput) commandOutput;

        List<Song> songs = new java.util.ArrayList<>(MyDatabase.getInstance()
                .getSongs());

        songs.sort((song, t1) -> {
            if (song.likesNo() > t1.likesNo()) {
                return -1;
            }
            if (song.likesNo().equals(t1.likesNo())) {
                return 0;
            }
            return 1;
        });

        final int limit = 5;
        output.result = songs.stream().map(AudioFile::getName).limit(limit)
                .toList();
    }

    @Override
    public GetTop5SongsOutput getCommandOutput() {
        return (GetTop5SongsOutput) this.commandOutput;
    }

    public static final class GetTop5SongsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new GetTop5SongsCommand(this);
        }
    }

    public static final class GetTop5SongsOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        private String user;
        @JsonIgnore
        private String message;
        private List<String> result;

        public GetTop5SongsOutput(final CommandInput commandInput) {
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
