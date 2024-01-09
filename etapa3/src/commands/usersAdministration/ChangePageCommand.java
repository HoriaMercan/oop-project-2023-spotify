package commands.usersAdministration;

import commands.AbstractCommand;
import databases.MyDatabase;
import entities.requirements.RequireOnline;
import entities.users.User;
import entities.users.functionalities.PageHandler;
import lombok.Getter;
import lombok.Setter;
import pagesystem.EnumPages;

public final class ChangePageCommand extends AbstractCommand implements RequireOnline {
    public ChangePageCommand(final ChangePageInput changePageInput) {
        super(changePageInput);
        this.commandOutput = new ChangePageOutput(changePageInput);
    }

    @Override
    public void executeCommand() {
        ChangePageInput input = (ChangePageInput) this.commandInput;
        ChangePageOutput output = (ChangePageOutput) this.commandOutput;

        User user = MyDatabase.getInstance().findUserByUsername(input.getUsername());
        String next = input.getNextPage();
        assert user != null;
        PageHandler pH = user.getPageHandler();
        assert pH != null;

        boolean playingSong = user.getPlayer().getTypeLoaded().equals("album")
                || user.getPlayer().getTypeLoaded().equals("song")
                || user.getPlayer().getTypeLoaded().equals("playlist");

        boolean playingPodcast = user.getPlayer().getTypeLoaded().equals("podcast");
        if (next.equalsIgnoreCase("Home")
                || next.equalsIgnoreCase("LikedContent")
                || (next.equals("Artist") && (pH.hasPage(EnumPages.ARTIST) || playingSong))
                || (next.equals("Host") && (pH.hasPage(EnumPages.HOST)
                || playingPodcast))) {
            EnumPages page = EnumPages.HOME;
            switch (input.getNextPage()) {
                case "LikedContent":
                    page = EnumPages.LIKED_CONTENT;
                    break;
                case "Artist":
                    if (playingSong) {
                        pH.addPage(EnumPages.ARTIST, () -> {
                            String artistName = user.getPlayer()
                                    .getCurrentPlayed().getCreator();
                            return MyDatabase.getInstance()
                                    .findArtistByUsername(artistName)
                                    .getPageContent();
                        });
                    }
                    page = EnumPages.ARTIST;
                    break;
                case "Host":
                    if (playingPodcast) {
                        String hostName = user.getPlayer().getCurrentPlayed().getCreator();
                        pH.addPage(EnumPages.HOST, () ->
                                MyDatabase.getInstance().findHostByUsername(hostName)
                                .getPageContent());
                    }
                    page = EnumPages.HOST;
                    break;
                default:
            }

            pH.setCurrentPage(page);
            pH.resetNextPage();
            output.setMessage(input.getUsername()
                    + " accessed " + input.nextPage + " successfully.");
            return;
        }

        output.setMessage(input.getUsername() + " is trying to access a non-existent page.");
    }

    @Override
    public ChangePageOutput getCommandOutput() {
        return (ChangePageOutput) this.commandOutput;
    }

    public static final class ChangePageInput extends AbstractCommand.CommandInput {
        @Setter
        @Getter
        private String nextPage;

        @Override
        public AbstractCommand getCommandFromInput() {
            return new ChangePageCommand(this);
        }
    }

    public static final class ChangePageOutput extends AbstractCommand.CommandOutput {
        public ChangePageOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}

