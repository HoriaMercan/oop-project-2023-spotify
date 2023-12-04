package commands.admin;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Artist;
import gateways.AdminAPI;
import lombok.Getter;
import lombok.Setter;

public final class RemoveAlbumCommand extends AbstractCommand {
    public RemoveAlbumCommand(final RemoveAlbumInput removeAlbumInput) {
        super(removeAlbumInput);
        this.commandOutput = new RemoveAlbumOutput(removeAlbumInput);
    }

    @Override
    public void executeCommand() {
        RemoveAlbumInput input = (RemoveAlbumInput) this.commandInput;
        RemoveAlbumOutput output = (RemoveAlbumOutput) this.commandOutput;

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

        if (!artist.hasAlbum(input.name)) {
            output.setMessage(input.getUsername() + " doesn't have an album with the given name.");
            return;
        }

        Album album = (Album) AdminAPI.getAudioCollectionWithNameFromCreator(artist, input.name);
        assert album != null;
        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());
        if (!AdminAPI.getUsersListeningToAudioCollection(album).isEmpty()) {
            output.setMessage(artist.getUsername() + " can't delete this album.");
            return;
        }

        AdminAPI.removeAudioCollectionFromCreator(artist, album);
        output.setMessage(artist.getUsername() + " deleted the album successfully.");
    }

    @Override
    public RemoveAlbumOutput getCommandOutput() {
        return (RemoveAlbumOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class RemoveAlbumInput extends AbstractCommand.CommandInput {
        private String name;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new RemoveAlbumCommand(this);
        }
    }

    public static final class RemoveAlbumOutput extends AbstractCommand.CommandOutput {
        public RemoveAlbumOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

