package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.AbstractUser;
import entities.users.Artist;
import entities.audioCollections.AudioCollection;
import gateways.AdminAPI;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class GetTop5ArtistsCommand extends AbstractCommand {
    public GetTop5ArtistsCommand(final GetTop5ArtistsInput getTop5ArtistsInput) {
        super(getTop5ArtistsInput);
        this.commandOutput = new GetTop5ArtistsOutput(getTop5ArtistsInput);
    }

    @Override
    public void executeCommand() {
        GetTop5ArtistsOutput output = (GetTop5ArtistsOutput) commandOutput;

        List<Artist> artists = MyDatabase.getInstance().getArtists();

        artists.sort(new Comparator<Artist>() {
            @Override
            public int compare(final Artist artist, final Artist t1) {

                int likes1 = AdminAPI.getArtistLikes(artist);
                int likes2 = AdminAPI.getArtistLikes(t1);
                return Integer.compare(likes2, likes1);
            }
        });
        final int limit = 5;
        output.result = artists.stream().map(AbstractUser::getUsername).limit(limit)
                .toList();
    }

    @Override
    public GetTop5ArtistsOutput getCommandOutput() {
        return (GetTop5ArtistsOutput) this.commandOutput;
    }

    public static final class GetTop5ArtistsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new GetTop5ArtistsCommand(this);
        }
    }

    public static final class GetTop5ArtistsOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        protected String user;
        @JsonIgnore
        protected String message;

        @Getter
        @Setter
        private List<String> result;

        public GetTop5ArtistsOutput(final CommandInput commandInput) {
            super(commandInput);
        }
        public void setResult(final List<String> result) {
            this.result = result;
        }
    }
}