package commands.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import entities.User.UserPlayer1;
import gateways.PlayerAPI;

public class StatusCommand extends AbstractCommand {
	public StatusCommand(StatusInput statusInput) {
		super(statusInput);
		this.commandOutput = new StatusOutput(statusInput);
	}

	public static class StatusInput extends AbstractCommand.CommandInput {
		@Override
		public AbstractCommand getCommandFromInput() {
			return new StatusCommand(this);
		}
	}


	public static class StatusOutput extends AbstractCommand.CommandOutput {
		@JsonIgnore
		private String message;
		private Stats stats = new Stats();
		public class Stats {
			private String name;
			private Integer remainedTime;
			private String repeat;
			private boolean shuffle;

			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public Integer getRemainedTime() {
				return remainedTime;
			}
			public void setRemainedTime(Integer remainedTime) {
				this.remainedTime = remainedTime;
			}
			public String getRepeat() {
				return repeat;
			}
			public void setRepeat(String repeat) {
				this.repeat = repeat;
			}
			public boolean isShuffle() {
				return shuffle;
			}
			public void setShuffle(boolean shuffle) {
				this.shuffle = shuffle;
			}
			public boolean isPaused() {
				return paused;
			}
			public void setPaused(boolean paused) {
				this.paused = paused;
			}

			private boolean paused;
			Stats(){}
			Stats(UserPlayer1 userPlayer, Integer currentTime) {
				this.name = "";
			}

		}

		public Stats getStats() {
			return stats;
		}

		public void setStats(Stats stats) {
			this.stats = stats;
		}
		public StatusOutput(CommandInput commandInput) {
			super(commandInput);
		}
	}


	public StatusOutput getCommandOutput() {
		return (StatusOutput) this.commandOutput;
	}

	@Override
	public void executeCommand() {
		StatusInput input = (StatusInput) this.commandInput;
		StatusOutput output = (StatusOutput) this.commandOutput;

		PlayerAPI.setStatus(output.getStats(), input.getUsername(), input.getTimestamp());
	}
}
