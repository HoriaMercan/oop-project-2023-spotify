package commands.searchbar;

import commands.AbstractCommand;
import entities.helpers.Filter;
import gateways.SearchBarAPI;

import java.util.ArrayList;
import java.util.List;

public final class SearchCommand extends AbstractCommand {
    public SearchCommand(final SearchInput searchInput) {
        super(searchInput);
        this.commandOutput = new SearchOutput(searchInput);
    }

    @Override
    public void executeCommand() {
        SearchInput input = (SearchInput) this.commandInput;
        SearchOutput output = (SearchOutput) this.commandOutput;
        String type = input.getType();
        List<String> results = switch (type) {
            case "song" -> SearchBarAPI.getSongsByFilter(input.getUsername(),
                    input.getTimestamp(), input.getFilters());
            case "playlist" -> SearchBarAPI.getPlaylistsByFilter(input.getUsername(),
                    input.getTimestamp(), input.getFilters());
            case "podcast" -> SearchBarAPI.getPodcastsByFilter(input.getUsername(),
                    input.getTimestamp(), input.getFilters());
            default -> new ArrayList<>();
        };

        output.setResults(results);
        output.setMessage("Search returned " + results.size() + " results");
    }

    @Override
    public SearchOutput getCommandOutput() {
        return (SearchOutput) this.commandOutput;
    }

    public static final class SearchInput extends AbstractCommand.CommandInput {
        private Filter filters;
        private String type;

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }

        public Filter getFilters() {
            return filters;
        }

        public void setFilters(final Filter filters) {
            this.filters = filters;
        }

        @Override
        public SearchCommand getCommandFromInput() {
            return new SearchCommand(this);
        }
    }

    public static final class SearchOutput extends AbstractCommand.CommandOutput {

        private List<String> results;

        public SearchOutput(final CommandInput commandInput) {
            super(commandInput);
        }

        public List<String> getResults() {
            return results;
        }

        public void setResults(final List<String> results) {
            this.results = results;
        }
    }
}
