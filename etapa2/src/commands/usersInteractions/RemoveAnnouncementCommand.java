package commands.usersInteractions;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Host;
import lombok.Getter;
import lombok.Setter;

public final class RemoveAnnouncementCommand extends AbstractCommand {
    public RemoveAnnouncementCommand(final RemoveAnnouncementInput removeAnnouncementInput) {
        super(removeAnnouncementInput);
        this.commandOutput = new RemoveAnnouncementOutput(removeAnnouncementInput);
    }

    @Override
    public void executeCommand() {
        RemoveAnnouncementInput input = (RemoveAnnouncementInput) this.commandInput;
        RemoveAnnouncementOutput output = (RemoveAnnouncementOutput) this.commandOutput;

        AbstractUser user = MyDatabase.getInstance()
                .findAbstractUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The username " + input.getUsername() + " doesn't exist.");
            return;
        }

        if (!user.getUserType().equals(UserType.HOST)) {
            output.setMessage(input.getUsername() + " is not a host.");
            return;
        }

        Host host = (Host) user;

        if (!host.hasAnnouncement(input.getName())) {
            output.setMessage(host.getUsername() + " has no announcement with the given name.");
            return;
        }

        host.removeAnnouncement(input.getName());
        output.setMessage(host.getUsername()
                + " has successfully deleted the announcement.");
    }

    @Override
    public RemoveAnnouncementOutput getCommandOutput() {
        return (RemoveAnnouncementOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class RemoveAnnouncementInput extends AbstractCommand.CommandInput {
        private String name;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new RemoveAnnouncementCommand(this);
        }
    }

    public static final class RemoveAnnouncementOutput extends AbstractCommand.CommandOutput {
        public RemoveAnnouncementOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

