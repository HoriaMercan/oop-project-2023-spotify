package commands.monetization;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.users.User;
import gateways.AdminAPI;
import lombok.Getter;
import lombok.Setter;

public final class AdBreakCommand extends AbstractCommand {
    public AdBreakCommand(final AdBreakInput commandInput) {
        super(commandInput);
        commandOutput = new AdBreakOutput(commandInput);
    }

    @Override
    public void executeCommand() {
        AdBreakInput input = (AdBreakInput) commandInput;
        AdBreakOutput output = (AdBreakOutput) commandOutput;

        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The user %s doesn't exist.".formatted(input.getUsername()));
            return;
        }

        if (user.getPlayer().getTypeLoaded().equals("podcast")
                || user.getPlayer().getTypeLoaded().isEmpty()) {
            output.setMessage("%s is not playing any music.".formatted(user.getUsername()));
            return;
        }

//        user.getPayment().payToArtists(1.0 * input.getPrice());
        user.getPayment().setTotalMoney(1.0 * input.getPrice());
        // de schimbat aici, plata se face cand incepe ad-ul
        user.getPlayer().setNextAd();
        output.setMessage("Ad inserted successfully.");
    }

    public static final class AdBreakInput extends CommandInput {
        @Setter
        @Getter
        private Integer price;
        @Override
        public AbstractCommand getCommandFromInput() {
            return new AdBreakCommand(this);
        }
    }

    public static final class AdBreakOutput extends CommandOutput {
        public AdBreakOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
