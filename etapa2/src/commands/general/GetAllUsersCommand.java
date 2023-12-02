package commands.general;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.requirements.RequireOnline;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class GetAllUsersCommand extends AbstractCommand implements RequireOnline {
    public GetAllUsersCommand(final GetAllUsersInput getAllUsersInput) {
        super(getAllUsersInput);
        this.commandOutput = new GetAllUsersOutput(getAllUsersInput);
    }

    @Override
    public void executeCommand() {
        GetAllUsersInput input = (GetAllUsersInput) this.commandInput;
        GetAllUsersOutput output = (GetAllUsersOutput) this.commandOutput;

        output.setResult(MyDatabase.getInstance().getAllAbstractUserNames());
    }

    @Override
    public GetAllUsersOutput getCommandOutput() {
        return (GetAllUsersOutput) this.commandOutput;
    }

    public static final class GetAllUsersInput extends AbstractCommand.CommandInput {
        @JsonIgnore
        private String username;


        @Override
        public AbstractCommand getCommandFromInput() {
            return new GetAllUsersCommand(this);
        }
    }

    public static final class GetAllUsersOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        private String user;

        @JsonIgnore
        private String message;

        @Setter
        @Getter
        private List<String> result;
        public GetAllUsersOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

