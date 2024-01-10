package commands.monetization;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.helpers.Merch;
import entities.users.Artist;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;
import pagesystem.EnumPages;

public final class BuyMerchCommand extends AbstractCommand {
    public BuyMerchCommand(final BuyMerchCommand.BuyMerchInput commandInput) {
        super(commandInput);
        commandOutput = new BuyMerchCommand.BuyMerchOutput(commandInput);
    }

    @Override
    public void executeCommand() {
        BuyMerchCommand.BuyMerchInput input = (BuyMerchCommand.BuyMerchInput) commandInput;
        BuyMerchCommand.BuyMerchOutput output = (BuyMerchCommand.BuyMerchOutput) commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());

        if (user == null) {
            output.setMessage("The username %s doesn't exist."
                    .formatted(input.getUsername()));
            return;
        }

        EnumPages currPage = user.getPageHandler().getCurrentPage();
        if (!(currPage.equals(EnumPages.ARTIST))) {
            output.setMessage("Cannot buy merch from this page.");
            return;
        }

        String name = user.getPageHandler().getContentCreatorPage();

        Artist artist = MyDatabase.getInstance().findArtistByUsername(name);
        assert artist != null;

        if (!artist.getMerches().stream().map(Merch::getName)
                .toList().contains(input.getName())) {
            output.setMessage("The merch %s doesn't exist.".formatted(input.getName()));
            return;
        }

        Merch merch =
                artist.getMerches().stream()
                        .filter(merch1 -> merch1.getName().equals(input.getName()))
                        .toList().get(0);

        assert merch != null;
        user.getPayment().buyMerch(artist, merch);
        output.setMessage("%s has added new merch successfully."
                .formatted(user.getUsername()));
    }

    public static final class BuyMerchInput extends CommandInput {
        @Getter
        @Setter
        private String name;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new BuyMerchCommand(this);
        }
    }

    public static final class BuyMerchOutput extends CommandOutput {
        public BuyMerchOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
