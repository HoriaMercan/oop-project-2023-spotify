package commands.notifications;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.AbstractUser;
import entities.users.ContentCreator;
import entities.users.User;
import gateways.AdminAPI;
import pagesystem.EnumPages;

public class SubscribeCommand extends AbstractCommand {
    public SubscribeCommand(final SubscribeCommand.SubscribeInput SubscribeInput) {
        super(SubscribeInput);
        this.commandOutput = new SubscribeCommand.SubscribeOutput(SubscribeInput);
    }

    @Override
    public void executeCommand() {
        SubscribeInput input = (SubscribeInput) commandInput;
        SubscribeOutput output = (SubscribeOutput) commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The username %s doesn't exist."
                    .formatted(input.getUsername()));
            return;
        }

        EnumPages currPage = user.getPageHandler().getCurrentPage();
        if (!(currPage.equals(EnumPages.ARTIST) || currPage.equals(EnumPages.HOST))) {
            output.setMessage("To subscribe you need to be on the page of an artist or host.");
            return;
        }

        String name = user.getPageHandler().getContentCreatorPage();

        ContentCreator creator = switch (currPage) {
            case ARTIST -> MyDatabase.getInstance().findArtistByUsername(name);
            case HOST -> MyDatabase.getInstance().findHostByUsername(name);
            default -> throw new IllegalStateException("Unexpected value: " + currPage);
        };

        assert creator != null;
        boolean subscribed = creator.addSubscriber(user);

        String verb = subscribed ? "subscribed to" : "unsubscribed from";

        String creatorName = ((AbstractUser) creator).getUsername();

        output.setMessage("%s %s %s successfully."
                .formatted(user.getUsername(), verb, creatorName));
    }


    public static final class SubscribeInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new SubscribeCommand(this);
        }
    }

    public static final class SubscribeOutput extends AbstractCommand.CommandOutput {
        public SubscribeOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

