package commands.recommendation;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.requirements.RequireOnline;
import entities.users.User;
import gateways.AdminAPI;


public class LoadRecommendationsCommand extends AbstractCommand implements RequireOnline {
    public LoadRecommendationsCommand(final LoadRecommendationsCommand.LoadRecommendationsInput LoadRecommendationsInput) {
        super(LoadRecommendationsInput);
        this.commandOutput = new LoadRecommendationsCommand.LoadRecommendationsOutput(LoadRecommendationsInput);
    }

    @Override
    public void executeCommand() {
        LoadRecommendationsCommand.LoadRecommendationsInput input = (LoadRecommendationsCommand.LoadRecommendationsInput) commandInput;
        LoadRecommendationsCommand.LoadRecommendationsOutput output = (LoadRecommendationsCommand.LoadRecommendationsOutput) commandOutput;

        String verifyOnline = isUserOnline(input.getUsername());
        if (!verifyOnline.isEmpty()) {
            output.setMessage(verifyOnline);
            return;
        }

        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());
        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());

        assert user != null;

        if (user.getSongRecommendations().isEmpty()
                && user.getPlaylistRecommendations().isEmpty()) {
            output.setMessage("No recommendations available.");
            return;
        }

        user.loadRecommendation();
        output.setMessage("Playback loaded successfully.");

    }


    public static final class LoadRecommendationsInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new LoadRecommendationsCommand(this);
        }
    }

    public static final class LoadRecommendationsOutput extends AbstractCommand.CommandOutput {

        public LoadRecommendationsOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

