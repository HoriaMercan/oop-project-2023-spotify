package commands.usersAdministration;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.requirements.RequireOnline;
import entities.users.User;
import entities.users.functionalities.PageHandler;
import lombok.Getter;
import lombok.Setter;
import pagesystem.EnumPages;

public final class ChangePageCommand extends AbstractCommand implements RequireOnline {
    public ChangePageCommand(final ChangePageInput changePageInput) {
        super(changePageInput);
        this.commandOutput = new ChangePageOutput(changePageInput);
    }

    @Override
    public void executeCommand() {
        ChangePageInput input = (ChangePageInput) this.commandInput;
        ChangePageOutput output = (ChangePageOutput) this.commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        String next = input.getNextPage();
        assert user != null;
        PageHandler pH = user.getPageHandler();
        assert pH != null;
        if (next.equalsIgnoreCase("Home")
                || next.equalsIgnoreCase("LikedContent")
                || (next.equals("Artist") && pH.hasPage(EnumPages.ARTIST))
                || (next.equals("Host") && pH.hasPage(EnumPages.HOST))) {
            EnumPages page = EnumPages.HOME;
            switch (input.getNextPage()) {
                case "LikedContent":
                    page = EnumPages.LIKED_CONTENT;
                    break;
                case "Artist":
                    page = EnumPages.ARTIST;
                    break;
                case "Host":
                    page = EnumPages.HOST;
                    break;
                default:
            }

            pH.setCurrentPage(page);
            output.setMessage(input.getUsername()
                    + " accessed " + input.nextPage + " successfully.");
            return;
        }

        output.setMessage(input.getUsername() + " is trying to access a non-existent page.");
    }

    @Override
    public ChangePageOutput getCommandOutput() {
        return (ChangePageOutput) this.commandOutput;
    }

    public static final class ChangePageInput extends AbstractCommand.CommandInput {
        @Setter
        @Getter
        private String nextPage;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new ChangePageCommand(this);
        }
    }

    public static final class ChangePageOutput extends AbstractCommand.CommandOutput {
        public ChangePageOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

