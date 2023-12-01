package commands.player;

import commands.AbstractCommand;
import gateways.PlayerAPI;

public final class AddRemoveInPlaylistCommand extends AbstractCommand {
    public AddRemoveInPlaylistCommand(final AddRemoveInPlaylistInput addRemoveInPlaylistInput) {
        super(addRemoveInPlaylistInput);
        this.commandOutput = new AddRemoveInPlaylistOutput(addRemoveInPlaylistInput);
    }

    @Override
    public void executeCommand() {
        AddRemoveInPlaylistInput input = (AddRemoveInPlaylistInput) this.commandInput;
        AddRemoveInPlaylistOutput output = (AddRemoveInPlaylistOutput) this.commandOutput;

        output.setMessage(
                PlayerAPI.getAddRemoveMessage(
                        input.getUsername(), input.getTimestamp(), input.getPlaylistId()));
    }

    @Override
    public AddRemoveInPlaylistOutput getCommandOutput() {
        return (AddRemoveInPlaylistOutput) this.commandOutput;
    }

    public static final class AddRemoveInPlaylistInput extends AbstractCommand.CommandInput {
        private Integer playlistId;

        public Integer getPlaylistId() {
            return this.playlistId;
        }

        public void setPlaylistId(final Integer playlistId) {
            this.playlistId = playlistId;
        }

        @Override
        public AbstractCommand getCommandFromInput() {
            return new AddRemoveInPlaylistCommand(this);
        }
    }

    public static final class AddRemoveInPlaylistOutput
            extends AbstractCommand.CommandOutput {
        public AddRemoveInPlaylistOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
