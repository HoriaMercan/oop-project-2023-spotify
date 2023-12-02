package commands.admin;

import commands.AbstractCommand;
import lombok.Getter;
import lombok.Setter;

public final class RemoveAnnouncementCommand extends AbstractCommand {
    public RemoveAnnouncementCommand(final RemoveAnnouncementInput removeAnnouncementInput) {
        super(removeAnnouncementInput);
        this.commandOutput = new RemoveAnnouncementOutput(removeAnnouncementInput);
    }

    @Override
    public void executeCommand() {
        RemoveAnnouncementInput input = (RemoveAnnouncementInput) this.commandInput;
        RemoveAnnouncementOutput output = (RemoveAnnouncementOutput) this.commandOutput;


    }

    @Override
    public RemoveAnnouncementOutput getCommandOutput() {
        return (RemoveAnnouncementOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class RemoveAnnouncementInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new RemoveAnnouncementCommand(this);
        }
    }

    public static final class RemoveAnnouncementOutput extends AbstractCommand.CommandOutput {
        public RemoveAnnouncementOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

