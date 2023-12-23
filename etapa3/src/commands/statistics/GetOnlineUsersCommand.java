package commands.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.User;
import java.util.List;

public final class GetOnlineUsersCommand extends AbstractCommand {
    public GetOnlineUsersCommand(final GetOnlineUsersInput getOnlineUsersInput) {
        super(getOnlineUsersInput);
        this.commandOutput = new GetOnlineUsersOutput(getOnlineUsersInput);
    }

    @Override
    public void executeCommand() {
        GetOnlineUsersOutput output = (GetOnlineUsersOutput) commandOutput;

        List<String> onlineUsers = MyDatabase.getInstance().getUsers().stream()
                .filter(User::isOnline).map(User::getUsername).toList();

        output.setResult(onlineUsers);
    }

    @Override
    public GetOnlineUsersOutput getCommandOutput() {
        return (GetOnlineUsersOutput) this.commandOutput;
    }

    public static final class GetOnlineUsersInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new GetOnlineUsersCommand(this);
        }
    }

    public static final class GetOnlineUsersOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        protected String user;
        @JsonIgnore
        protected String message;
        private List<String> result;

        public GetOnlineUsersOutput(final CommandInput commandInput) {
            super(commandInput);
        }

        public List<String> getResult() {
            return result;
        }
        public void setResult(final List<String> result) {
            this.result = result;
        }
    }
}

