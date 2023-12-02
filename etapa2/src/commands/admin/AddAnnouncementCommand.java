package commands.admin;

import commands.AbstractCommand;
import lombok.Getter;
import lombok.Setter;

public final class AddAnnouncementCommand extends AbstractCommand {
    public AddAnnouncementCommand(final AddAnnouncementInput addAnnouncementInput) {
        super(addAnnouncementInput);
        this.commandOutput = new AddAnnouncementOutput(addAnnouncementInput);
    }

    @Override
    public void executeCommand() {
        AddAnnouncementInput input = (AddAnnouncementInput) this.commandInput;
        AddAnnouncementOutput output = (AddAnnouncementOutput) this.commandOutput;


    }

    @Override
    public AddAnnouncementOutput getCommandOutput() {
        return (AddAnnouncementOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class AddAnnouncementInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new AddAnnouncementCommand(this);
        }
    }

    public static final class AddAnnouncementOutput extends AbstractCommand.CommandOutput {
        public AddAnnouncementOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

