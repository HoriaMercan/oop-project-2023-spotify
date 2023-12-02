package commands.admin;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.requirements.RequireOnline;
import entities.users.AbstractUser;
import entities.users.Artist;
import entities.users.Host;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;

public final class AddUserCommand extends AbstractCommand {
    public AddUserCommand(final AddUserInput addUserInput) {
        super(addUserInput);
        this.commandOutput = new AddUserOutput(addUserInput);
    }

    @Override
    public void executeCommand() {
        AddUserInput input = (AddUserInput) this.commandInput;
        AddUserOutput output = (AddUserOutput) this.commandOutput;

        AbstractUser newUser =
                MyDatabase.getInstance().findAbstractUserByUsername(input.getUsername());
        if (newUser != null) {
            output.setMessage("The username " + input.getUsername() + " is already taken.");
            return;
        }

        newUser = switch (input.getType()) {
            case "user" -> new User(input.getUsername(), input.getCity(), input.getAge());
            case "artist" -> new Artist(input.getUsername(), input.getCity(), input.getAge());
            case "host" -> new Host(input.getUsername(), input.getCity(), input.getAge());
            default -> null;
        };

        MyDatabase.getInstance().addUser(newUser);
        output.setMessage("The username " + input.getUsername() + " has been added" +
                " successfully.");

    }

    @Override
    public AddUserOutput getCommandOutput() {
        return (AddUserOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class AddUserInput extends AbstractCommand.CommandInput {
        private String type;
        private Integer age;
        private String city;
        @Override
        public AbstractCommand getCommandFromInput() {
            return new AddUserCommand(this);
        }
    }

    public static final class AddUserOutput extends AbstractCommand.CommandOutput {
        public AddUserOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
