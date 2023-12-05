package commands.usersAdministration;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.requirements.RequireOnline;
import entities.users.User;


public final class PrintCurrentPageCommand extends AbstractCommand implements RequireOnline {
    public PrintCurrentPageCommand(final PrintCurrentPageInput printCurrentPageInput) {
        super(printCurrentPageInput);
        this.commandOutput = new PrintCurrentPageOutput(printCurrentPageInput);
    }

    @Override
    public void executeCommand() {
        PrintCurrentPageInput input = (PrintCurrentPageInput) this.commandInput;
        PrintCurrentPageOutput output = (PrintCurrentPageOutput) this.commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        assert user != null;
        output.setMessage(user.getPageHandler().getCurrentPageContent());
    }

    @Override
    public PrintCurrentPageOutput getCommandOutput() {
        return (PrintCurrentPageOutput) this.commandOutput;
    }

    public static final class PrintCurrentPageInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new PrintCurrentPageCommand(this);
        }
    }

    public static final class PrintCurrentPageOutput extends AbstractCommand.CommandOutput {
        public PrintCurrentPageOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
