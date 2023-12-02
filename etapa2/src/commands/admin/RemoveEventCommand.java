package commands.admin;

import commands.AbstractCommand;
import lombok.Getter;
import lombok.Setter;

public final class RemoveEventCommand extends AbstractCommand {
    public RemoveEventCommand(final RemoveEventInput removeEventInput) {
        super(removeEventInput);
        this.commandOutput = new RemoveEventOutput(removeEventInput);
    }

    @Override
    public void executeCommand() {
        RemoveEventInput input = (RemoveEventInput) this.commandInput;
        RemoveEventOutput output = (RemoveEventOutput) this.commandOutput;


    }

    @Override
    public RemoveEventOutput getCommandOutput() {
        return (RemoveEventOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class RemoveEventInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new RemoveEventCommand(this);
        }
    }

    public static final class RemoveEventOutput extends AbstractCommand.CommandOutput {
        public RemoveEventOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

