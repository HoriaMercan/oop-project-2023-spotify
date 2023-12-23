package commands.usersInteractions;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Event;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Artist;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class AddEventCommand extends AbstractCommand {
    public AddEventCommand(final AddEventInput addEventInput) {
        super(addEventInput);
        this.commandOutput = new AddEventOutput(addEventInput);
    }

    @Override
    public void executeCommand() {
        AddEventInput input = (AddEventInput) this.commandInput;
        AddEventOutput output = (AddEventOutput) this.commandOutput;

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

        if (artist.getEvents().stream().map(Event::getName).toList()
                .contains(input.getName())) {
            output.setMessage(input.getUsername() + " has another event with the same name.");
            return;
        }

        Date date;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = dateFormat.parse(input.getDate());
            if (!dateFormat.format(date).equals(input.getDate())) {
                output.setMessage("Event for "
                        + input.getUsername() + " does not have a valid date.");
                return;
            }
        } catch (ParseException e) {
            output.setMessage("Event for " + input.getUsername() + " does not have a valid date.");
            return;
        }

        artist.getEvents().add(new Event(input.name, input.description, input.date));
        output.setMessage(input.getUsername() + " has added new event successfully.");
    }

    @Override
    public AddEventOutput getCommandOutput() {
        return (AddEventOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class AddEventInput extends AbstractCommand.CommandInput {
        private String name;
        private String date;
        private String description;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new AddEventCommand(this);
        }
    }

    public static final class AddEventOutput extends AbstractCommand.CommandOutput {
        public AddEventOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
