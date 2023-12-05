package commands.usersInteractions;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.ContentCreator;
import entities.users.User;
import gateways.AdminAPI;
import pagesystem.EnumPages;

import java.util.List;

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

        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());

        if (newUser.getUserType().equals(UserType.NORMAL)) {
            if (!AdminAPI.getUListeningToUPlaylists((User) newUser).isEmpty()) {
                output.setMessage(input.getUsername() + " can't be deleted.");
                return;
            }
            AdminAPI.removeNormalUser(newUser);
            output.setMessage(input.getUsername() + " was successfully deleted.");
            return;
        }

        List<User> listeningTo = AdminAPI.getUsersListeningToCreator((ContentCreator) newUser);

        List<User> usersHavingPage =
                AdminAPI.getOnlineUsers().stream().filter(user -> user.getPageHandler()
                        .getContentCreatorPage().equals(newUser.getUsername())).toList();

        List<User> usersHavingPageActive = usersHavingPage.stream()
                .filter(user -> user.getPageHandler().getCurrentPage().equals(EnumPages.HOST)
                || user.getPageHandler().getCurrentPage().equals(EnumPages.ARTIST)).toList();
        System.out.println(listeningTo);
        if (!listeningTo.isEmpty() || !usersHavingPageActive.isEmpty()) {
            usersHavingPage.forEach(user -> user.getPageHandler().removeNonStandardPages());
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
            default -> {

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

