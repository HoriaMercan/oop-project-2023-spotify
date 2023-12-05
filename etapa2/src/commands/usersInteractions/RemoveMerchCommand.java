package commands.usersInteractions;

import commands.AbstractCommand;
import lombok.Getter;
import lombok.Setter;

public final class RemoveMerchCommand extends AbstractCommand {
    public RemoveMerchCommand(final RemoveMerchInput removeMerchInput) {
        super(removeMerchInput);
        this.commandOutput = new RemoveMerchOutput(removeMerchInput);
    }

    @Override
    public void executeCommand() {
        RemoveMerchInput input = (RemoveMerchInput) this.commandInput;
        RemoveMerchOutput output = (RemoveMerchOutput) this.commandOutput;


    }

    @Override
    public RemoveMerchOutput getCommandOutput() {
        return (RemoveMerchOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class RemoveMerchInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new RemoveMerchCommand(this);
        }
    }

    public static final class RemoveMerchOutput extends AbstractCommand.CommandOutput {
        public RemoveMerchOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

