package commands;

import commands.player.PlayPauseCommand;

public class MainCommands {

	public static void main(String[] args) {
		AbstractCommand.CommandInput c = new PlayPauseCommand.PlayPauseInput();
		AbstractCommand a = c.getCommandFromInput();
		a.executeCommand();
	}
}
