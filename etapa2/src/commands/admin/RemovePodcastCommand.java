package commands.admin;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.audioCollections.Podcast;
import entities.audioFiles.PodcastEpisode;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.Host;
import gateways.AdminAPI;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class RemovePodcastCommand extends AbstractCommand {
    public RemovePodcastCommand(final RemovePodcastInput removePodcastInput) {
        super(removePodcastInput);
        this.commandOutput = new RemovePodcastOutput(removePodcastInput);
    }

    @Override
    public void executeCommand() {
        RemovePodcastInput input = (RemovePodcastInput) this.commandInput;
        RemovePodcastOutput output = (RemovePodcastOutput) this.commandOutput;

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
        if (!host.hasPodcast(input.name)) {
            output.setMessage(host.getUsername() + " doesn't have a podcast with the given name.");
            return;
        }
    }

    @Override
    public RemovePodcastOutput getCommandOutput() {
        return (RemovePodcastOutput) this.commandOutput;
    }

    @Getter
    @Setter
    public static final class RemovePodcastInput extends AbstractCommand.CommandInput {
        private String name;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new RemovePodcastCommand(this);
        }
    }

    public static final class RemovePodcastOutput extends AbstractCommand.CommandOutput {
        public RemovePodcastOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
