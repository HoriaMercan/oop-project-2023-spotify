package commands.usersAdministration;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.requirements.RequireOnline;
import entities.users.User;

public final class PreviousPageCommand extends AbstractCommand implements RequireOnline {
    public PreviousPageCommand(final PreviousPageInput previousPageInput) {
        super(previousPageInput);
        this.commandOutput = new PreviousPageOutput(previousPageInput);
    }

    @Override
    public void executeCommand() {
        PreviousPageInput input = (PreviousPageInput) this.commandInput;
        PreviousPageOutput output = (PreviousPageOutput) this.commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        assert user != null;
        boolean bool = user.getPageHandler().doBack();

        if (bool) {
            output.setMessage("The user %s has navigated successfully to the previous page."
                    .formatted(input.getUsername()));
        } else {
            output.setMessage("There are no pages left to go back.");
        }
    }

    @Override
    public PreviousPageCommand.PreviousPageOutput getCommandOutput() {
        return (PreviousPageCommand.PreviousPageOutput) this.commandOutput;
    }

    public static final class PreviousPageInput extends AbstractCommand.CommandInput {

        @Override
        public AbstractCommand getCommandFromInput() {
            return new PreviousPageCommand(this);
        }
    }

    public static final class PreviousPageOutput extends AbstractCommand.CommandOutput {
        public PreviousPageOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

