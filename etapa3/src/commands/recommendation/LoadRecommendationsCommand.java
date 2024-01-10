package commands.recommendation;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.requirements.RequireOnline;
import entities.users.User;
import gateways.AdminAPI;


public final class LoadRecommendationsCommand extends AbstractCommand implements RequireOnline {
    public LoadRecommendationsCommand(final LoadRecommendationsInput loadRecommendationsInput) {
        super(loadRecommendationsInput);
        this.commandOutput =
                new LoadRecommendationsOutput(loadRecommendationsInput);
    }

    @Override
    public void executeCommand() {
        LoadRecommendationsInput input = (LoadRecommendationsInput) commandInput;
        LoadRecommendationsOutput output = (LoadRecommendationsOutput) commandOutput;

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

