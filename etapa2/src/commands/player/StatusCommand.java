package commands.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import commands.AbstractCommand;
import gateways.PlayerAPI;

public final class StatusCommand extends AbstractCommand {
    public StatusCommand(final StatusInput statusInput) {
        super(statusInput);
        this.commandOutput = new StatusOutput(statusInput);
    }

    @Override
    public void executeCommand() {
        StatusInput input = (StatusInput) this.commandInput;
        StatusOutput output = (StatusOutput) this.commandOutput;

        PlayerAPI.setStatus(output.getStats(), input.getUsername(), input.getTimestamp());
    }

    public StatusOutput getCommandOutput() {
        return (StatusOutput) this.commandOutput;
    }

    public static final class StatusInput extends AbstractCommand.CommandInput {
        @Override
        public AbstractCommand getCommandFromInput() {
            return new StatusCommand(this);
        }
    }

    public static final class StatusOutput extends AbstractCommand.CommandOutput {
        @JsonIgnore
        private String message;
        private Stats stats = new Stats();

        public StatusOutput(final CommandInput commandInput) {
            super(commandInput);
        }

        public Stats getStats() {
            return stats;
        }

        public void setStats(final Stats stats) {
            this.stats = stats;
        }

        public final class Stats {
            private String name;
            private Integer remainedTime;
            private String repeat;
            private boolean shuffle;
            private boolean paused;

            Stats() {
            }



            public String getName() {
                return name;
            }

            public void setName(final String name) {
                this.name = name;
            }

            public Integer getRemainedTime() {
                return remainedTime;
            }

            public void setRemainedTime(final Integer remainedTime) {
                this.remainedTime = remainedTime;
            }

            public String getRepeat() {
                return repeat;
            }

            public void setRepeat(final String repeat) {
                this.repeat = repeat;
            }

            public boolean isShuffle() {
                return shuffle;
            }

            public void setShuffle(final boolean shuffle) {
                this.shuffle = shuffle;
            }

            public boolean isPaused() {
                return paused;
            }

            public void setPaused(final boolean paused) {
                this.paused = paused;
            }

        }
    }
}
