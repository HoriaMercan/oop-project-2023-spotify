package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioFiles.AudioFile;
import java.util.ArrayList;
import java.util.List;

public final class ShowPreferredSongsCommand extends AbstractCommand {
    public ShowPreferredSongsCommand(final ShowPreferredSongsInput showPreferredSongsInput) {
        super(showPreferredSongsInput);
        this.commandOutput = new ShowPreferredSongsOutput(showPreferredSongsInput);
    }

    @Override
    public void executeCommand() {
        ShowPreferredSongsInput input = (ShowPreferredSongsInput) commandInput;
        ShowPreferredSongsOutput output = (ShowPreferredSongsOutput) commandOutput;

        output.result = MyDatabase.getInstance().findUserByUsername(input.getUsername())
                .getLikedSongs().stream().map(AudioFile::getName).toList();
    }

    @Override
    public ShowPreferredSongsOutput getCommandOutput() {
        return (ShowPreferredSongsOutput) commandOutput;
    }

    public static final class ShowPreferredSongsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new ShowPreferredSongsCommand(this);
        }
    }

    public static final class ShowPreferredSongsOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        private String message;
        private List<String> result = new ArrayList<>();

        public ShowPreferredSongsOutput(final CommandInput commandInput) {
            super(commandInput);
        }

        public List<String> getResult() {
            return result;
        }
    }

    private class SongTimestamp implements Comparable {
        protected String songName;
        protected Integer timestamp;

        SongTimestamp(final String songName, final Integer timestamp) {
            this.songName = songName;
            this.timestamp = timestamp;
        }

        public String getSongName() {
            return songName;
        }

        @Override
        public int compareTo(final Object o) {
            SongTimestamp st = (SongTimestamp) o;
            if (this.timestamp < st.timestamp) {
                return -1;
            }
            if (this.timestamp.equals(st.timestamp)) {
                return this.songName.compareTo(st.songName);
            }
            return 1;
        }
    }
}
