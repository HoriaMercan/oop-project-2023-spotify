package commands.usersAdministration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioFiles.AudioFile;
import entities.users.Artist;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

public final class ShowAlbumsCommand extends AbstractCommand {
    public ShowAlbumsCommand(final ShowAlbumsInput showAlbumsInput) {
        super(showAlbumsInput);
        this.commandOutput = new ShowAlbumsOutput(showAlbumsInput);
    }

    @Override
    public void executeCommand() {
        ShowAlbumsInput input = (ShowAlbumsInput) this.commandInput;
        ShowAlbumsOutput output = (ShowAlbumsOutput) this.commandOutput;

        Artist artist = (Artist) MyDatabase.getInstance()
                .findAbstractUserByUsername(input.getUsername());
        assert artist != null;
        List<Album> albums = artist.getAlbums();
        assert albums != null;

        for (Album album : albums) {
            output.getResult().add(new ShowAlbumsOutputFormat(
                    album.getName(),
                    album.getSongs().stream().map(AudioFile::getName).toList()
            ));
        }
    }

    @Override
    public ShowAlbumsOutput getCommandOutput() {
        return (ShowAlbumsOutput) this.commandOutput;
    }

    public static final class ShowAlbumsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new ShowAlbumsCommand(this);
        }
    }

    public static final class ShowAlbumsOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        private String message;

        @Getter
        private List<ShowAlbumsOutputFormat> result = new ArrayList<>();

        public ShowAlbumsOutput(final CommandInput commandInput) {
            super(commandInput);
        }

        public void setResults(final List<ShowAlbumsOutputFormat> results) {
            this.result = results;
        }
    }

    @Getter
    @Setter
    public final class ShowAlbumsOutputFormat {
        private String name;
        private List<String> songs;

        public ShowAlbumsOutputFormat() {
        }

        public ShowAlbumsOutputFormat(final String name, final List<String> songs) {
            this.name = name;
            this.songs = songs;
        }
    }
}
