package commands.searchbar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Filter;
import gateways.SearchBarAPI;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

public class SearchCommand extends AbstractCommand {
	public SearchCommand(SearchInput searchInput) {
		super(searchInput);
		this.commandOutput = new SearchOutput(searchInput);
	}

	public static class SearchInput extends AbstractCommand.CommandInput {
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Filter getFilters() {
			return filters;
		}

		public void setFilters(Filter filters) {
			this.filters = filters;
		}

		private String type;
		Filter filters;

		@Override
		public SearchCommand getCommandFromInput() {
			return new SearchCommand(this);
		}
	}




	public static class SearchOutput extends AbstractCommand.CommandOutput {

		public SearchOutput(CommandInput commandInput) {
			super(commandInput);
		}

		public List<String> getResults() {
			return results;
		}

		public void setResults(List<String>results) {
			this.results = results;
		}

		public List<String> results;
	}

	@Override
	public void executeCommand() {
		SearchInput input = (SearchInput) this.commandInput;
		SearchOutput output = (SearchOutput) this.commandOutput;
		String type = input.getType();
		List<String> results = switch (type) {
			case "song" -> SearchBarAPI.getSongsByFilter(input.getUsername(), input.getFilters());
			case "playlist" -> SearchBarAPI.getPlaylistsByFilter(input.getUsername(), input.getFilters());
			case "podcast" -> SearchBarAPI.getPodcastsByFilter(input.getUsername(), input.getFilters());
			default -> new ArrayList<>();
		};

		output.setResults(results);
		output.setMessage("Search returned " + results.size() + " results");
	}

	@Override
	public SearchOutput getCommandOutput() {
		return (SearchOutput) this.commandOutput;
	}
}
