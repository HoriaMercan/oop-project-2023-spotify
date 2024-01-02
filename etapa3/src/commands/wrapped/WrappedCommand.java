package commands.wrapped;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.AbstractUser;
import entities.users.User;
import entities.wrapper.handlers.AbstractDataWrapping;
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

        AbstractUser user = MyDatabase.getInstance()
                .findAbstractUserByUsername(input.getUsername());

        assert user != null; // Change if necessary

        output.result = user.getWrapperStatistics().getDataWrapping();
    }


    public static final class WrappedInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new WrappedCommand(this);
        }
    }

    public static final class WrappedOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        private String message;

        @Getter
        AbstractDataWrapping result;
        public WrappedOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
