package commands.searchbar;

import commands.AbstractCommand;
import entities.requirements.RequireOnline;
import gateways.SearchBarAPI;

import java.util.ArrayList;

public final class SelectCommand extends AbstractCommand implements RequireOnline {
    public SelectCommand(final SelectInput selectInput) {
        super(selectInput);
        this.commandOutput = new SelectOutput(selectInput);
    }

    @Override
    public void executeCommand() {
        SelectInput input = (SelectInput) commandInput;
        SelectOutput output = (SelectOutput) commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }
        output.setMessage(SearchBarAPI.getSelectionMessage(input.getUsername(),
                input.getItemNumber()));
    }

    public SelectOutput getCommandOutput() {
        return (SelectOutput) this.commandOutput;
    }

    public static final class SelectInput extends AbstractCommand.CommandInput {
        private Integer itemNumber;

        public SelectInput() {
            super();
        }

        public SelectInput(final String username, final Integer timestamp) {
            super(username, timestamp);
        }

        public Integer getItemNumber() {
            return this.itemNumber;
        }

        public void setItemNumber(final Integer itemNumber) {
            this.itemNumber = itemNumber;
        }

        @Override
        public AbstractCommand getCommandFromInput() {
            return new SelectCommand(this);
        }
    }

    public static final class SelectOutput extends AbstractCommand.CommandOutput {
        public SelectOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
