package commands.general;

import commands.AbstractCommand;

import databases.MyDatabase;
import entities.users.AbstractUser;
import entities.users.AbstractUser.UserType;
import entities.users.User;

public final class SwitchConnectionStatusCommand extends AbstractCommand {
    public SwitchConnectionStatusCommand(final SwitchConnectionStatusInput switchConnInput) {
        super(switchConnInput);
        this.commandOutput = new SwitchConnectionStatusOutput(switchConnInput);
    }

    @Override
    public void executeCommand() {
        SwitchConnectionStatusInput input = (SwitchConnectionStatusInput) this.commandInput;
        SwitchConnectionStatusOutput output = (SwitchConnectionStatusOutput) this.commandOutput;

        AbstractUser absUser =
                MyDatabase.getInstance().findAbstractUserByUsername(input.getUsername());
        System.out.println(input.getUsername());
        if (absUser == null) {
            output.setMessage("The username " + input.getUsername() + " doesn't exist.");
            return;
        }

        if (!absUser.getUserType().equals(UserType.NORMAL)) {
            output.setMessage(input.getUsername() + " is not a normal user.");
            return;
        }
        User user = (User) absUser;
        if (user.isOnline()) {
            user.getPlayer().updatePlayer(input.getTimestamp());
        }
        user.setOnline(!user.isOnline());
        user.getPlayer().setLastUpdatedTime(input.getTimestamp());
        output.setMessage(input.getUsername() + " has changed status successfully.");
    }

    @Override
    public SwitchConnectionStatusOutput getCommandOutput() {
        return (SwitchConnectionStatusOutput) this.commandOutput;
    }

    public static final class SwitchConnectionStatusInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new SwitchConnectionStatusCommand(this);
        }
    }

    public static final class SwitchConnectionStatusOutput extends AbstractCommand.CommandOutput {
        public SwitchConnectionStatusOutput(final CommandInput commandInput) {
            super(commandInput);
        }
    }
}
