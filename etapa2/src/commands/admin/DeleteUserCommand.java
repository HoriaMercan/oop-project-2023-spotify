package commands.admin;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioCollections.AudioCollection;
import entities.audioCollections.Playlist;
import entities.audioFiles.AudioFile;
import entities.audioFiles.Song;
import entities.users.*;
import entities.users.AbstractUser.UserType;
import entities.users.functionalities.UserPlayer;
import gateways.AdminAPI;
import gateways.PlayerAPI;
import page_system.EnumPages;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class DeleteUserCommand extends AbstractCommand {
    public DeleteUserCommand(final DeleteUserInput deleteUserInput) {
        super(deleteUserInput);
        this.commandOutput = new DeleteUserOutput(deleteUserInput);
    }

    @Override
    public void executeCommand() {
        DeleteUserInput input = (DeleteUserInput) this.commandInput;
        DeleteUserOutput output = (DeleteUserOutput) this.commandOutput;

        AbstractUser newUser =
                MyDatabase.getInstance().findAbstractUserByUsername(input.getUsername());
        if (newUser == null) {
            output.setMessage("The username " + input.getUsername() + " doesn't exist.");
            return;
        }

        if (newUser.getUserType().equals(UserType.NORMAL)) {
            AdminAPI.removeNormalUser(newUser);
            output.setMessage(input.getUsername() + " was successfully deleted.");
            return;
        }

        List<User> onlineUsers = AdminAPI.getOnlineUsers();
        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());

        List<User> listeningTo = AdminAPI.getUsersListeningToCreator((ContentCreator) newUser);

        List<User> usersHavingPage =
                AdminAPI.getOnlineUsers().stream().filter(user -> user.getPageHandler()
                        .getContentCreatorPage().equals(newUser.getUsername())).toList();
        System.out.println(listeningTo);
        if (!listeningTo.isEmpty() || !usersHavingPage.isEmpty()) {
            output.setMessage(input.getUsername() + " can't be deleted.");
            return;
        }

        /* Delete all active pages with this Content Creator from users */

        switch (newUser.getUserType()) {
            case ARTIST -> {
                AdminAPI.removeArtist(newUser);
            }
            case HOST -> {
                AdminAPI.removeHost(newUser);
            }
        }
        output.setMessage(input.getUsername() + " was successfully deleted.");
    }

    @Override
    public DeleteUserOutput getCommandOutput() {
        return (DeleteUserOutput) this.commandOutput;
    }


    public static final class DeleteUserInput extends AbstractCommand.CommandInput {

        @Override
        public AbstractCommand getCommandFromInput() {
            return new DeleteUserCommand(this);
        }
    }

    public static final class DeleteUserOutput extends AbstractCommand.CommandOutput {
        public DeleteUserOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

