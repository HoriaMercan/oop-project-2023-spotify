package commands.usersInteractions;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.User;
import gateways.AdminAPI;

public final class DeleteUserCommand extends AbstractCommand {
    public DeleteUserCommand(final DeleteUserInput deleteUserInput) {
        super(deleteUserInput);
        this.commandOutput = new DeleteUserOutput(deleteUserInput);
    }

    @Override
    public void executeCommand() {
        DeleteUserInput input = (DeleteUserInput) this.commandInput;
        DeleteUserOutput output = (DeleteUserOutput) this.commandOutput;

        AbstractUser absUser =
                MyDatabase.getInstance().findAbstractUserByUsername(input.getUsername());
        if (absUser == null) {
            output.setMessage("The username " + input.getUsername() + " doesn't exist.");
            return;
        }
        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());

        if (absUser.getUserType().equals(UserType.NORMAL)) {
            if (!AdminAPI.getUListeningToUPlaylists((User) absUser).isEmpty()) {
                output.setMessage(input.getUsername() + " can't be deleted.");
                return;
            }
            AdminAPI.removeNormalUser(absUser);
            output.setMessage(input.getUsername() + " was successfully deleted.");
            return;
        }

        if (AdminAPI.userInteractWithOther(absUser)) {
            output.setMessage(input.getUsername() + " can't be deleted.");
            return;
        }

        /* Delete all active pages with this Content Creator from users */
        switch (absUser.getUserType()) {
            case ARTIST -> AdminAPI.removeArtist(absUser);
            case HOST -> AdminAPI.removeHost(absUser);
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

