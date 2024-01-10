package commands.notifications;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Notification;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class GetNotificationsCommand extends AbstractCommand {
    public GetNotificationsCommand(final GetNotificationsInput getNotificationsInput) {
        super(getNotificationsInput);
        this.commandOutput = new GetNotificationsOutput(getNotificationsInput);
    }

    @Override
    public void executeCommand() {
        GetNotificationsInput input = (GetNotificationsInput) commandInput;
        GetNotificationsOutput output = (GetNotificationsOutput) commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());

        assert user != null;

        output.notifications = user.getNotificationsHandler().getNotifications();
        user.getNotificationsHandler().deleteAllNotifications();
    }


    public static final class GetNotificationsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new GetNotificationsCommand(this);
        }
    }

    public static final class GetNotificationsOutput extends AbstractCommand.CommandOutput {

        @Getter
        @Setter
        private List<Notification> notifications;

        public GetNotificationsOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}


