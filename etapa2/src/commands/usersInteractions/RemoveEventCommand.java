package commands.usersInteractions;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Event;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Artist;
import lombok.Getter;
import lombok.Setter;

public final class RemoveEventCommand extends AbstractCommand {
    public RemoveEventCommand(final RemoveEventInput removeEventInput) {
        super(removeEventInput);
        this.commandOutput = new RemoveEventOutput(removeEventInput);
    }

    @Override
    public void executeCommand() {
        RemoveEventInput input = (RemoveEventInput) this.commandInput;
        RemoveEventOutput output = (RemoveEventOutput) this.commandOutput;

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

        Event event = artist.hasEvent(input.getName());
        if (event == null) {
            output.setMessage(input.getUsername() + " doesn't have an event with the given name.");
            return;
        }

        artist.getEvents().remove(event);
        output.setMessage(input.getUsername() + " deleted the event successfully.");
    }

    @Override
    public RemoveEventOutput getCommandOutput() {
        return (RemoveEventOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class RemoveEventInput extends AbstractCommand.CommandInput {
        private String name;
        @Override
        public AbstractCommand getCommandFromInput() {
            return new RemoveEventCommand(this);
        }
    }

    public static final class RemoveEventOutput extends AbstractCommand.CommandOutput {
        public RemoveEventOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

