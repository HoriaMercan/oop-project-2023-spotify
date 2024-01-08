package commands.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Notification;
import entities.users.AbstractUser;
import entities.users.ContentCreator;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GetNotificationsCommand extends AbstractCommand {
    public GetNotificationsCommand(final GetNotificationsCommand.GetNotificationsInput GetNotificationsInput) {
        super(GetNotificationsInput);
        this.commandOutput = new GetNotificationsCommand.GetNotificationsOutput(GetNotificationsInput);
    }

    @Override
    public void executeCommand() {
        GetNotificationsCommand.GetNotificationsInput input = (GetNotificationsCommand.GetNotificationsInput) commandInput;
        GetNotificationsCommand.GetNotificationsOutput output = (GetNotificationsCommand.GetNotificationsOutput) commandOutput;

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


