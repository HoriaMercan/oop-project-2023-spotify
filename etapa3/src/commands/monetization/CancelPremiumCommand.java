package commands.monetization;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.monetization.UserPayment.AccountType;
import entities.users.User;
import gateways.AdminAPI;

public class CancelPremiumCommand extends AbstractCommand {
    public CancelPremiumCommand(CancelPremiumInput commandInput) {
        super(commandInput);
        commandOutput = new CancelPremiumOutput(commandInput);
    }

    @Override
    public void executeCommand() {
        CancelPremiumInput input = (CancelPremiumInput) commandInput;
        CancelPremiumOutput output = (CancelPremiumOutput) commandOutput;

        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The user %s doesn't exist.".formatted(input.getUsername()));
            return;
        }

        if (user.getPayment().getAccountType().equals(AccountType.ADS_ENJOYED)) {
            output.setMessage("%s is not a premium user."
                    .formatted(input.getUsername()));
            return;
        }

        user.getPayment().changeAccountType();
        output.setMessage("%s cancelled the subscription successfully."
                .formatted(input.getUsername()));
    }
    public static final class CancelPremiumInput extends CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new CancelPremiumCommand(this);
        }
    }

    public static final class CancelPremiumOutput extends CommandOutput {
        public CancelPremiumOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
