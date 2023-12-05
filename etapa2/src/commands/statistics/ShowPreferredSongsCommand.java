package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioFiles.AudioFile;
import entities.audioFiles.Song;

import java.util.ArrayList;
import java.util.Comparator;
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

//        List<Song> songs = MyDatabase.getInstance().getSongs();
//
//        ArrayList<SongTimestamp> stList = new ArrayList<>();
//        for (Song song : songs) {
//            if (song.isSongLikedByUser(commandInput.getUsername())) {
//                stList.add(new SongTimestamp(song.getName(),
//                        song.getTimestampOfLike(input.getUsername())));
//            }
//        }

        output.result  = MyDatabase.getInstance().findUserByUsername(input.getUsername())
                .getLikedSongs().stream().map(AudioFile::getName).toList();

//        output.result = stList.stream().sorted().map(SongTimestamp::getSongName).toList();
//        output.result = songs.stream()
//                .filter(song -> song.isSongLikedByUser(input.getUsername()))
//                .sorted(new Comparator<Song>() {
//                    @Override
//                    public int compare(Song song, Song t1) {
//                        return Integer.compare(t1.likesNo(), song.likesNo());
//                    }
//                }).map(AudioFile::getName).toList();
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
