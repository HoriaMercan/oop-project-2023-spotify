package commands.monetization;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Merch;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public final class SeeMerchCommand extends AbstractCommand {
    public SeeMerchCommand(final SeeMerchCommand.SeeMerchInput commandInput) {
        super(commandInput);
        commandOutput = new SeeMerchCommand.SeeMerchOutput(commandInput);
    }

    @Override
    public void executeCommand() {
        SeeMerchCommand.SeeMerchInput input = (SeeMerchCommand.SeeMerchInput) commandInput;
        SeeMerchCommand.SeeMerchOutput output = (SeeMerchCommand.SeeMerchOutput) commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The username %s doesn't exist."
                    .formatted(input.getUsername()));
            return;
        }

        output.result = user.getPayment().getBoughtMerch().stream()
                .map(Merch::getName).toList();

    }

    public static final class SeeMerchInput extends CommandInput {
        @Getter
        @Setter
        private String name;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new SeeMerchCommand(this);
        }
    }

    public static final class SeeMerchOutput extends CommandOutput {
        @Getter
        @Setter
        private List<String> result;
        public SeeMerchOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
