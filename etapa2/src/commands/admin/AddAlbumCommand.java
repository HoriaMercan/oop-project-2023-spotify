package commands.admin;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioFiles.Song;
import entities.requirements.RequireOnline;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Artist;
import entities.users.Host;
import entities.users.User;
import gateways.AdminAPI;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class AddAlbumCommand extends AbstractCommand {
    public AddAlbumCommand(final AddAlbumInput addAlbumInput) {
        super(addAlbumInput);
        this.commandOutput = new AddAlbumOutput(addAlbumInput);
    }

    @Override
    public void executeCommand() {
        AddAlbumInput input = (AddAlbumInput) this.commandInput;
        AddAlbumOutput output = (AddAlbumOutput) this.commandOutput;

        AbstractUser user = MyDatabase.getInstance()
                .findAbstractUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The username " + input.getUsername() + " doesn't exist.");
            return;
        }
        if (!user.getUserType().equals(UserType.ARTIST)) {
            output.setMessage(input.getUsername() + " is not an artist.");
            return;
        }
        Artist artist = (Artist) user;

        if (artist.hasAlbum(input.name)) {
            output.setMessage(input.getUsername() + " has another album with the same name.");
            return;
        }
        if (AdminAPI.audioFilesRepeated(input.getSongs())) {
            output.setMessage(input.getUsername()
                    + " has the same song at least twice in this album.");
            return;
        }

        Album newAlbum = new Album(input.getName(), input.getUsername(), input.releaseYear,
                input.getDescription(), input.getSongs());
        artist.addAlbum(newAlbum);

        MyDatabase.getInstance().getAlbums().add(newAlbum);
        MyDatabase.getInstance().getSongs().addAll(input.getSongs());

        output.setMessage(input.getUsername() + " has added new album successfully.");
    }

    @Override
    public AddAlbumOutput getCommandOutput() {
        return (AddAlbumOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class AddAlbumInput extends AbstractCommand.CommandInput {
        private String name;
        private Integer releaseYear;
        private String description;
        private List<Song> songs;
        @Override
        public AbstractCommand getCommandFromInput() {
            return new AddAlbumCommand(this);
        }
    }

    public static final class AddAlbumOutput extends AbstractCommand.CommandOutput {
        public AddAlbumOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
