package commands;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import commands.player.AddRemoveInPlaylistCommand;
import commands.player.BackwardCommand;
import commands.player.ForwardCommand;
import commands.player.LikeCommand;
import commands.player.LoadCommand;
import commands.player.NextCommand;
import commands.player.PlayPauseCommand;
import commands.player.PrevCommand;
import commands.player.RepeatCommand;
import commands.player.ShuffleCommand;
import commands.player.StatusCommand;
import commands.playlist.CreatePlaylistCommand;
import commands.playlist.FollowPlaylistCommand;
import commands.playlist.ShowPlaylistsCommand;
import commands.playlist.SwitchVisibilityCommand;
import commands.searchbar.SearchCommand;
import commands.searchbar.SelectCommand;
import commands.statistics.GetTop5PlaylistsCommand;
import commands.statistics.GetTop5SongsCommand;
import commands.statistics.ShowPreferredSongsCommand;

/**
 * Base class of all commands
 */
public abstract class AbstractCommand {
    protected final CommandInput commandInput;
    protected CommandOutput commandOutput;

    public AbstractCommand(final CommandInput commandInput) {
        this.commandInput = commandInput;
        this.commandOutput = new CommandOutput(commandInput);
    }

    /**
     * This function makes all the necessary changes in all the databases
     * and generates output
     */
    public void executeCommand() {
    }

    /**
     * @return generic commandOutput
     */
    public CommandOutput getCommandOutput() {
        return new CommandOutput();
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "command")
    @JsonSubTypes({
            @Type(value = SearchCommand.SearchInput.class, name = "search"),
            @Type(value = SelectCommand.SelectInput.class, name = "select"),
            @Type(value = LoadCommand.LoadInput.class, name = "load"),
            @Type(value = PlayPauseCommand.PlayPauseInput.class, name = "playPause"),
            @Type(value = RepeatCommand.RepeatInput.class, name = "repeat"),
            @Type(value = ShuffleCommand.ShuffleInput.class, name = "shuffle"),
            @Type(value = SelectCommand.SelectInput.class, name = "select"),
            @Type(value = ForwardCommand.ForwardInput.class, name = "forward"),
            @Type(value = BackwardCommand.BackwardInput.class, name = "backward"),
            @Type(value = LikeCommand.LikeInput.class, name = "like"),
            @Type(value = NextCommand.NextInput.class, name = "next"),
            @Type(value = PrevCommand.PrevInput.class, name = "prev"),
            @Type(value = AddRemoveInPlaylistCommand.AddRemoveInPlaylistInput.class,
                    name = "addRemoveInPlaylist"),
            @Type(value = StatusCommand.StatusInput.class, name = "status"),
            @Type(value = CreatePlaylistCommand.CreatePlaylistInput.class,
                    name = "createPlaylist"),
            @Type(value = SwitchVisibilityCommand.SwitchVisibilityInput.class,
                    name = "switchVisibility"),
            @Type(value = FollowPlaylistCommand.FollowPlaylistInput.class,
                    name = "follow"),
            @Type(value = ShowPlaylistsCommand.ShowPlaylistsInput.class,
                    name = "showPlaylists"),
            @Type(value = ShowPreferredSongsCommand.ShowPreferredSongsInput.class,
                    name = "showPreferredSongs"),
            @Type(value = GetTop5SongsCommand.GetTop5SongsInput.class,
                    name = "getTop5Songs"),
            @Type(value = GetTop5PlaylistsCommand.GetTop5PlaylistsInput.class,
                    name = "getTop5Playlists"),
    })
    public static class CommandInput {
        private String username;
        private Integer timestamp;

        @JsonIgnore
        public CommandInput(final String username, final Integer timestamp) {
            this.timestamp = timestamp;
            this.username = username;
        }

        public CommandInput() {
        }

        public final String getUsername() {
            return this.username;
        }

        public final void setUsername(final String username) {
            this.username = username;
        }

        public final Integer getTimestamp() {
            return this.timestamp;
        }

        public final void setTimestamp(final Integer timestamp) {
            this.timestamp = timestamp;
        }

        /**
         * @return Generic type Command construction from input
         */
        @JsonIgnore
        public AbstractCommand getCommandFromInput() {
            return null;
        }

    }

    @JsonDeserialize()
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = As.PROPERTY,
            property = "command",
            defaultImpl = CommandOutput.class
    )
    @JsonSubTypes({
            @Type(value = SearchCommand.SearchOutput.class, name = "search"),
            @JsonSubTypes.Type(value = SelectCommand.SelectOutput.class,
                    name = "select"),
            @Type(value = LoadCommand.LoadOutput.class, name = "load"),
            @Type(value = PlayPauseCommand.PlayPauseOutput.class,
                    name = "playPause"),
            @Type(value = RepeatCommand.RepeatOutput.class,
                    name = "repeat"),
            @Type(value = ShuffleCommand.ShuffleOutput.class, name = "shuffle"),
            @Type(value = SelectCommand.SelectOutput.class, name = "select"),
            @Type(value = ForwardCommand.ForwardOutput.class, name = "forward"),
            @Type(value = BackwardCommand.BackwardOutput.class, name = "backward"),
            @Type(value = LikeCommand.LikeOutput.class, name = "like"),
            @Type(value = NextCommand.NextOutput.class, name = "next"),
            @Type(value = PrevCommand.PrevOutput.class, name = "prev"),
            @Type(value = AddRemoveInPlaylistCommand.AddRemoveInPlaylistOutput.class,
                    name = "addRemoveInPlaylist"),
            @Type(value = StatusCommand.StatusOutput.class, name = "status"),
            @Type(value = CreatePlaylistCommand.CreatePlaylistOutput.class,
                    name = "createPlaylist"),
            @Type(value = SwitchVisibilityCommand.SwitchVisibilityOutput.class,
                    name = "switchVisibility"),
            @Type(value = FollowPlaylistCommand.FollowPlaylistOutput.class,
                    name = "follow"),
            @Type(value = ShowPlaylistsCommand.ShowPlaylistsOutput.class,
                    name = "showPlaylists"),
            @Type(value = ShowPreferredSongsCommand.ShowPreferredSongsOutput.class,
                    name = "showPreferredSongs"),
            @Type(value = GetTop5SongsCommand.GetTop5SongsOutput.class,
                    name = "getTop5Songs"),
            @Type(value = GetTop5PlaylistsCommand.GetTop5PlaylistsOutput.class,
                    name = "getTop5Playlists"),
    })
    public static class CommandOutput {
        protected String user;
        protected Integer timestamp;
        protected String message;

        public CommandOutput() {
        }

        public CommandOutput(final CommandInput commandInput) {
            this.user = commandInput.username;
            this.timestamp = commandInput.timestamp;
        }

        public final String getUser() {
            return user;
        }

        public final Integer getTimestamp() {
            return timestamp;
        }

        public final String getMessage() {
            return message;
        }

        public final void setMessage(final String message) {
            this.message = message;
        }

    }
}
