package commands.recommendation;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.User;
import gateways.AdminAPI;
import lombok.Getter;
import lombok.Setter;


public class UpdateRecommendationsCommand extends AbstractCommand {
    public UpdateRecommendationsCommand(final UpdateRecommendationsCommand.UpdateRecommendationsInput UpdateRecommendationsInput) {
        super(UpdateRecommendationsInput);
        this.commandOutput = new UpdateRecommendationsCommand.UpdateRecommendationsOutput(UpdateRecommendationsInput);
    }

    @Override
    public void executeCommand() {
        UpdateRecommendationsCommand.UpdateRecommendationsInput input = (UpdateRecommendationsCommand.UpdateRecommendationsInput) commandInput;
        UpdateRecommendationsCommand.UpdateRecommendationsOutput output = (UpdateRecommendationsCommand.UpdateRecommendationsOutput) commandOutput;

        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());
        AbstractUser abstractUser = MyDatabase.getInstance().findAbstractUserByUsername(input.getUsername());
        if (abstractUser == null) {
            output.setMessage("The username %s doesn't exist.".formatted(input.getUsername()));
            return;
        }

        if (!abstractUser.getUserType().equals(UserType.NORMAL)) {
            output.setMessage("%s is not a normal user.".formatted(input.getUsername()));
            return;
        }

        User user = (User) abstractUser;

        boolean bool = false;
        switch (input.getRecommendationType()) {
            case "random_song": {
//                if(!user.updateSongRecommendations()) {
//                    output.setMessage("No new recommendations were found");
//                    return;
//                }
                bool = user.updateSongRecommendations();
                break;
            }
            case "random_playlist": {
                bool = user.updateRandomPlaylistRecommendations();
                break;
            }
            case "fans_playlist": {
                bool = user.updateFansPlaylistRecommendations();
                break;
            }
            default:
        }

        if (!bool) {
            output.setMessage("No new recommendations were found");
            return;
        }
        output.setMessage("The recommendations for user %s have been updated successfully."
                .formatted(input.getUsername()));
    }


    @Getter
    public static final class UpdateRecommendationsInput extends AbstractCommand.CommandInput {
        @Setter
        private String recommendationType;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new UpdateRecommendationsCommand(this);
        }
    }

    public static final class UpdateRecommendationsOutput extends AbstractCommand.CommandOutput {

        public UpdateRecommendationsOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
