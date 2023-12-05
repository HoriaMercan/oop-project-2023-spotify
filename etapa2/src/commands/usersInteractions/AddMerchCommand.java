package commands.usersInteractions;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Merch;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Artist;
import lombok.Getter;
import lombok.Setter;

public final class AddMerchCommand extends AbstractCommand {
    public AddMerchCommand(final AddMerchInput addMerchInput) {
        super(addMerchInput);
        this.commandOutput = new AddMerchOutput(addMerchInput);
    }

    @Override
    public void executeCommand() {
        AddMerchInput input = (AddMerchInput) this.commandInput;
        AddMerchOutput output = (AddMerchOutput) this.commandOutput;

        AbstractUser user = MyDatabase.getInstance()
                .findAbstractUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The username " + input.getUsername() + " doesn't exist.");
            return;
        }

        if (!user.getUserType().equals(UserType.ARTIST)) {
            output.setMessage("The username " + input.getUsername() + " is not an artist.");
            return;
        }

        Artist artist = (Artist) user;
        if (artist.getMerches().stream().map(Merch::getName).toList()
                .contains(input.getName())) {
            output.setMessage(input.getUsername() + " has merchandise with the same name.");
            return;
        }

        if (input.getPrice() < 0) {
            output.setMessage("Price for merchandise can not be negative.");
            return;
        }

        artist.getMerches().add(new Merch(input.name, input.description, input.price));
        output.setMessage(input.getUsername() + " has added new merchandise successfully.");
    }

    @Override
    public AddMerchOutput getCommandOutput() {
        return (AddMerchOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class AddMerchInput extends AbstractCommand.CommandInput {
        private String name;
        private Integer price;
        private String description;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new AddMerchCommand(this);
        }
    }

    public static final class AddMerchOutput extends AbstractCommand.CommandOutput {
        public AddMerchOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
