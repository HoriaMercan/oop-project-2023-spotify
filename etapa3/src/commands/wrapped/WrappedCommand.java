package commands.wrapped;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.AbstractUser;
import entities.users.User;
import entities.wrapper.handlers.AbstractDataWrapping;
import gateways.AdminAPI;
import lombok.Getter;


public final class WrappedCommand extends AbstractCommand{
    public WrappedCommand(final WrappedInput WrappedInput) {
        super(WrappedInput);
        this.commandOutput = new WrappedOutput(WrappedInput);
    }

    @Override
    public void executeCommand() {
        WrappedInput input = (WrappedInput) this.commandInput;
        WrappedOutput output = (WrappedOutput) this.commandOutput;

        AdminAPI.updateAllOnlineUserPlayers(commandInput.getTimestamp());
        AbstractUser user = MyDatabase.getInstance()
                .findAbstractUserByUsername(input.getUsername());

        assert user != null; // Change if necessary

        output.result = user.getWrapperStatistics().getDataWrapping();

        String type = switch (user.getUserType()) {
            case NORMAL -> "user";
            case ARTIST -> "artist";
            case HOST -> "host";
        };
        if (output.result == null) {
            output.setMessage("No data to show for %s ".formatted(type)
                    + commandInput.getUsername() + ".");
        }

    }


    public static final class WrappedInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new WrappedCommand(this);
        }
    }

    public static final class WrappedOutput extends AbstractCommand.CommandOutput {
        @JsonInclude(Include.NON_NULL)
        private String message = null;

        @Getter
        @JsonInclude(Include.NON_NULL)
        AbstractDataWrapping result = null;
        public WrappedOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
