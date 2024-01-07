package commands.monetization;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.monetization.UserPayment.AccountType;
import entities.users.User;
import gateways.AdminAPI;

public class BuyPremiumCommand extends AbstractCommand {
    public BuyPremiumCommand(final BuyPremiumInput commandInput) {
        super(commandInput);
        commandOutput = new BuyPremiumOutput(commandInput);
    }

    @Override
    public void executeCommand() {
        BuyPremiumInput input = (BuyPremiumInput) commandInput;
        BuyPremiumOutput output = (BuyPremiumOutput) commandOutput;

        AdminAPI.updateAllOnlineUserPlayers(input.getTimestamp());

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The user %s doesn't exist.".formatted(input.getUsername()));
            return;
        }

        if (user.getPayment().getAccountType().equals(AccountType.PREMIUM)) {
            output.setMessage("%s is already a premium user."
                    .formatted(input.getUsername()));
            return;
        }

        user.getPayment().changeAccountType();
        output.setMessage("%s bought the subscription successfully."
                .formatted(input.getUsername()));
    }
    public static final class BuyPremiumInput extends CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new BuyPremiumCommand(this);
        }
    }

    public static final class BuyPremiumOutput extends CommandOutput {
        public BuyPremiumOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
