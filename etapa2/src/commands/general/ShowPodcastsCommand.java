package commands.general;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Album;
import entities.audioCollections.Podcast;
import entities.audioFiles.AudioFile;
import entities.users.Artist;
import entities.users.Host;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public final class ShowPodcastsCommand extends AbstractCommand {
    public ShowPodcastsCommand(final ShowPodcastsInput showPodcastsInput) {
        super(showPodcastsInput);
        this.commandOutput = new ShowPodcastsOutput(showPodcastsInput);
    }

    @Override
    public void executeCommand() {
        ShowPodcastsInput input = (ShowPodcastsInput) this.commandInput;
        ShowPodcastsOutput output = (ShowPodcastsOutput) this.commandOutput;

        Host host = (Host) MyDatabase.getInstance()
                .findAbstractUserByUsername(input.getUsername());

        List<Podcast> podcasts = host.getPodcasts();

        for (Podcast p: podcasts) {
            output.getResult().add(new ShowPodcastsOutputFormat(
                    p.getName(),
                    p.getEpisodes().stream().map(AudioFile::getName).toList()
            ));
        }
    }

    @Getter
    @Setter
    public final class ShowPodcastsOutputFormat {
        private String name;
        private List<String> episodes;
        public ShowPodcastsOutputFormat(){}
        public ShowPodcastsOutputFormat(final String name, final List<String>episodes) {
            this.name = name;
            this.episodes = episodes;
        }
    }
    @Override
    public ShowPodcastsOutput getCommandOutput() {
        return (ShowPodcastsOutput) this.commandOutput;
    }

    public static final class ShowPodcastsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new ShowPodcastsCommand(this);
        }
    }

    public static final class ShowPodcastsOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        private String message;

        @Getter
        private List<ShowPodcastsOutputFormat> result = new ArrayList<>();

        public void setResults(final List<ShowPodcastsOutputFormat> results) {
            this.result = results;
        }
        public ShowPodcastsOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}