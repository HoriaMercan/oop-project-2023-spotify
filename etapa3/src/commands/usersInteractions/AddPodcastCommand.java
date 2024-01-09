package commands.usersInteractions;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Podcast;
import entities.audioFiles.PodcastEpisode;
import entities.helpers.Notification;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Host;
import gateways.AdminAPI;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class AddPodcastCommand extends AbstractCommand {
    public AddPodcastCommand(final AddPodcastInput addPodcastInput) {
        super(addPodcastInput);
        this.commandOutput = new AddPodcastOutput(addPodcastInput);
    }

    @Override
    public void executeCommand() {
        AddPodcastInput input = (AddPodcastInput) this.commandInput;
        AddPodcastOutput output = (AddPodcastOutput) this.commandOutput;

        AbstractUser user = MyDatabase.getInstance()
                .findAbstractUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The username " + input.getUsername() + " doesn't exist.");
            return;
        }
        if (!user.getUserType().equals(UserType.HOST)) {
            output.setMessage(input.getUsername() + " is not a host.");
            return;
        }
        Host host = (Host) user;
        if (host.hasPodcast(input.name)) {
            output.setMessage(input.getUsername() + " has another podcast with the same name.");
            return;
        }
        if (AdminAPI.audioFilesRepeated(input.getEpisodes())) {
            output.setMessage(input.getUsername()
                    + " has the same episode in this podcast.");
            return;
        }

        host.sendNotificationToSubscribers(
                new Notification("New Podcast", "New Podcast from %s."
                        .formatted(host.getUsername())));

        Podcast podcast = new Podcast(input.getName(), input.getUsername(), input.getEpisodes());
        host.getPodcasts().add(podcast);
        MyDatabase.getInstance().getPodcasts().add(podcast);
        output.setMessage(input.getUsername() + " has added new podcast successfully.");
    }

    @Override
    public AddPodcastOutput getCommandOutput() {
        return (AddPodcastOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class AddPodcastInput extends AbstractCommand.CommandInput {
        private String name;
        private String description;
        private List<PodcastEpisode> episodes;
        @Override
        public AbstractCommand getCommandFromInput() {
            return new AddPodcastCommand(this);
        }
    }

    public static final class AddPodcastOutput extends AbstractCommand.CommandOutput {
        public AddPodcastOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
