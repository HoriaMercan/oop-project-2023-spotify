package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioCollections.AudioCollection;
import gateways.AdminAPI;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class GetTop5AlbumsCommand extends AbstractCommand {
    public GetTop5AlbumsCommand(final GetTop5AlbumsInput getTop5AlbumsInput) {
        super(getTop5AlbumsInput);
        this.commandOutput = new GetTop5AlbumsOutput(getTop5AlbumsInput);
    }

    @Override
    public void executeCommand() {
        GetTop5AlbumsOutput output = (GetTop5AlbumsOutput) commandOutput;

        List<Album> albums = MyDatabase.getInstance().getAlbums();

        albums.sort((album, t1) -> {

            int likes1 = AdminAPI.getAlbumsLikes(album);
            int likes2 = AdminAPI.getAlbumsLikes(t1);

            if (likes1 == likes2) {
                return album.getName().compareTo(t1.getName());
            }
            return Integer.compare(likes2, likes1);
        });
        final int limit = 5;
        output.result = albums.stream().map(AudioCollection::getName).limit(limit)
                .toList();
    }

    @Override
    public GetTop5AlbumsOutput getCommandOutput() {
        return (GetTop5AlbumsOutput) this.commandOutput;
    }

    public static final class GetTop5AlbumsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new GetTop5AlbumsCommand(this);
        }
    }

    public static final class GetTop5AlbumsOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        protected String user;
        @JsonIgnore
        protected String message;

        @Getter
        @Setter
        private List<String> result;

        public GetTop5AlbumsOutput(final CommandInput commandInput) {
            super(commandInput);
        }

        public void setResult(final List<String> result) {
            this.result = result;
        }
    }
}
