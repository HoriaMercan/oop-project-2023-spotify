package commands.admin;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Announcement;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Host;
import lombok.Getter;
import lombok.Setter;

public final class AddAnnouncementCommand extends AbstractCommand {
    public AddAnnouncementCommand(final AddAnnouncementInput addAnnouncementInput) {
        super(addAnnouncementInput);
        this.commandOutput = new AddAnnouncementOutput(addAnnouncementInput);
    }

    @Override
    public void executeCommand() {
        AddAnnouncementInput input = (AddAnnouncementInput) this.commandInput;
        AddAnnouncementOutput output = (AddAnnouncementOutput) this.commandOutput;

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

        if (host.hasAnnouncement(input.getName())) {
            output.setMessage(host.getUsername()
                    + " has already added an announcement with this name.");
            return;
        }

        Announcement newA = new Announcement(input.getName(), input.getDescription());
        host.getAnnouncements().add(newA);

        output.setMessage(host.getUsername()+" has successfully added new announcement.");
    }

    @Override
    public AddAnnouncementOutput getCommandOutput() {
        return (AddAnnouncementOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class AddAnnouncementInput extends AbstractCommand.CommandInput {
        private String name;
        private String description;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new AddAnnouncementCommand(this);
        }
    }

    public static final class AddAnnouncementOutput extends AbstractCommand.CommandOutput {
        public AddAnnouncementOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

