package commands.general;


import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioFiles.AudioFile;
import entities.users.Artist;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public final class PrintCurrentPageCommand extends AbstractCommand {
    public PrintCurrentPageCommand(final PrintCurrentPageInput printCurrentPageInput) {
        super(printCurrentPageInput);
        this.commandOutput = new PrintCurrentPageOutput(printCurrentPageInput);
    }

    @Override
    public void executeCommand() {
        PrintCurrentPageInput input = (PrintCurrentPageInput) this.commandInput;
        PrintCurrentPageOutput output = (PrintCurrentPageOutput) this.commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        output.setMessage(user.getPageHandler().getCurrentPage());
    }

    @Override
    public PrintCurrentPageOutput getCommandOutput() {
        return (PrintCurrentPageOutput) this.commandOutput;
    }

    public static final class PrintCurrentPageInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new PrintCurrentPageCommand(this);
        }
    }

    public static final class PrintCurrentPageOutput extends AbstractCommand.CommandOutput {
        public PrintCurrentPageOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
