package commands.searchbar;

import commands.AbstractCommand;
import gateways.SearchBarAPI;

public final class SelectCommand extends AbstractCommand {
	public SelectCommand(SelectInput selectInput) {
		super(selectInput);
		this.commandOutput = new SelectOutput(selectInput);
	}

	public final static class SelectInput extends AbstractCommand.CommandInput {
		private Integer itemNumber;

		public SelectInput() {
			super();
		}

		public void setItemNumber(Integer itemNumber) {
			this.itemNumber = itemNumber;
		}

		public Integer getItemNumber() {
			return this.itemNumber;
		}

		public SelectInput(String username, Integer timestamp) {
			super(username, timestamp);
		}

		@Override
		public AbstractCommand getCommandFromInput() {
			return new SelectCommand(this);
		}
	}

	public final static class SelectOutput extends AbstractCommand.CommandOutput {
		public SelectOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}


	public SelectOutput getCommandOutput() {
		return (SelectOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		SelectInput input = (SelectInput) commandInput;
		SelectOutput output = (SelectOutput) commandOutput;

		output.setMessage(SearchBarAPI.getSelectionMessage(input.getUsername(),
				input.getItemNumber()));
	}
}
