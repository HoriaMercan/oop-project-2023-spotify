package commands.admin;

import commands.AbstractCommand;
import lombok.Getter;
import lombok.Setter;

public final class RemoveAlbumCommand extends AbstractCommand {
    public RemoveAlbumCommand(final RemoveAlbumInput removeAlbumInput) {
        super(removeAlbumInput);
        this.commandOutput = new RemoveAlbumOutput(removeAlbumInput);
    }

    @Override
    public void executeCommand() {
        RemoveAlbumInput input = (RemoveAlbumInput) this.commandInput;
        RemoveAlbumOutput output = (RemoveAlbumOutput) this.commandOutput;


    }

    @Override
    public RemoveAlbumOutput getCommandOutput() {
        return (RemoveAlbumOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class RemoveAlbumInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new RemoveAlbumCommand(this);
        }
    }

    public static final class RemoveAlbumOutput extends AbstractCommand.CommandOutput {
        public RemoveAlbumOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

