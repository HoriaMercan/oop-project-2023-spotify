package commands.usersAdministration;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.requirements.RequireOnline;
import entities.users.User;
public class NextPageCommand extends AbstractCommand implements RequireOnline {
    public NextPageCommand(final NextPageCommand.NextPageInput NextPageInput) {
        super(NextPageInput);
        this.commandOutput = new NextPageCommand.NextPageOutput(NextPageInput);
    }

    @Override
    public void executeCommand() {
        NextPageCommand.NextPageInput input = (NextPageCommand.NextPageInput) this.commandInput;
        NextPageCommand.NextPageOutput output = (NextPageCommand.NextPageOutput) this.commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        assert user != null;
        boolean bool = user.getPageHandler().doNext();

        if (bool) {
            output.setMessage("The user %s has navigated successfully to the next page."
                    .formatted(input.getUsername()));
        } else {
            output.setMessage("There are no pages left to go forward.");
        }
    }

    @Override
    public NextPageCommand.NextPageOutput getCommandOutput() {
        return (NextPageCommand.NextPageOutput) this.commandOutput;
    }

    public static final class NextPageInput extends AbstractCommand.CommandInput {

        @Override
        public AbstractCommand getCommandFromInput() {
            return new NextPageCommand(this);
        }
    }

    public static final class NextPageOutput extends AbstractCommand.CommandOutput {
        public NextPageOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

