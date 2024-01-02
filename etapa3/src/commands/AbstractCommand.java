package commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import commands.usersInteractions.AddAlbumCommand;
import commands.usersInteractions.AddAnnouncementCommand;
import commands.usersInteractions.AddEventCommand;
import commands.usersInteractions.AddMerchCommand;
import commands.usersInteractions.AddPodcastCommand;
import commands.usersInteractions.AddUserCommand;
import commands.usersInteractions.DeleteUserCommand;
import commands.usersInteractions.RemoveAlbumCommand;
import commands.usersInteractions.RemoveAnnouncementCommand;
import commands.usersInteractions.RemoveEventCommand;
import commands.usersInteractions.RemoveMerchCommand;
import commands.usersInteractions.RemovePodcastCommand;
import commands.usersAdministration.ChangePageCommand;
import commands.usersAdministration.GetAllUsersCommand;
import commands.usersAdministration.PrintCurrentPageCommand;
import commands.usersAdministration.ShowAlbumsCommand;
import commands.usersAdministration.ShowPodcastsCommand;
import commands.usersAdministration.SwitchConnectionStatusCommand;
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
import commands.statistics.GetOnlineUsersCommand;
import commands.statistics.GetTop5AlbumsCommand;
import commands.statistics.GetTop5ArtistsCommand;
import commands.statistics.GetTop5PlaylistsCommand;
import commands.statistics.GetTop5SongsCommand;
import commands.statistics.ShowPreferredSongsCommand;
import commands.wrapped.WrappedCommand;

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
        return this.commandOutput;
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
            @Type(value = GetTop5AlbumsCommand.GetTop5AlbumsInput.class,
                    name = "getTop5Albums"),
            @Type(value = GetTop5ArtistsCommand.GetTop5ArtistsInput.class,
                    name = "getTop5Artists"),
            @Type(value = SwitchConnectionStatusCommand.SwitchConnectionStatusInput.class,
                    name = "switchConnectionStatus"),
            @Type(value = GetOnlineUsersCommand.GetOnlineUsersInput.class,
                    name = "getOnlineUsers"),
            @Type(value = AddUserCommand.AddUserInput.class, name = "addUser"),
            @Type(value = AddAlbumCommand.AddAlbumInput.class, name = "addAlbum"),
            @Type(value = ShowAlbumsCommand.ShowAlbumsInput.class, name = "showAlbums"),
            @Type(value = PrintCurrentPageCommand.PrintCurrentPageInput.class,
                    name = "printCurrentPage"),
            @Type(value = AddEventCommand.AddEventInput.class, name = "addEvent"),
            @Type(value = AddMerchCommand.AddMerchInput.class, name = "addMerch"),
            @Type(value = GetAllUsersCommand.GetAllUsersInput.class, name = "getAllUsers"),
            @Type(value = DeleteUserCommand.DeleteUserInput.class, name = "deleteUser"),
            @Type(value = AddPodcastCommand.AddPodcastInput.class, name = "addPodcast"),
            @Type(value = ShowPodcastsCommand.ShowPodcastsInput.class, name = "showPodcasts"),
            @Type(value = RemoveAnnouncementCommand.RemoveAnnouncementInput.class,
                    name = "removeAnnouncement"),
            @Type(value = AddAnnouncementCommand.AddAnnouncementInput.class,
                    name = "addAnnouncement"),
            @Type(value = RemoveEventCommand.RemoveEventInput.class, name = "removeEvent"),
            @Type(value = RemoveMerchCommand.RemoveMerchInput.class, name = "removeMerch"),
            @Type(value = RemovePodcastCommand.RemovePodcastInput.class, name = "removePodcast"),
            @Type(value = RemoveAlbumCommand.RemoveAlbumInput.class, name = "removeAlbum"),
            @Type(value = ChangePageCommand.ChangePageInput.class, name = "changePage"),
            @Type(value = WrappedCommand.WrappedInput.class, name = "wrapped"),
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
    @JsonSubTypes(value = {
            @Type(value = SearchCommand.SearchOutput.class, name = "search"),
            @Type(value = SelectCommand.SelectOutput.class,
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
            @Type(value = GetTop5AlbumsCommand.GetTop5AlbumsOutput.class,
                    name = "getTop5Albums"),
            @Type(value = GetTop5ArtistsCommand.GetTop5ArtistsOutput.class,
                    name = "getTop5Artists"),
            @Type(value = SwitchConnectionStatusCommand.SwitchConnectionStatusOutput.class,
                    name = "switchConnectionStatus"),
            @Type(value = GetOnlineUsersCommand.GetOnlineUsersOutput.class,
                    name = "getOnlineUsers"),
            @Type(value = AddUserCommand.AddUserOutput.class, name = "addUser"),
            @Type(value = AddAlbumCommand.AddAlbumOutput.class, name = "addAlbum"),
            @Type(value = ShowAlbumsCommand.ShowAlbumsOutput.class, name = "showAlbums"),
            @Type(value = PrintCurrentPageCommand.PrintCurrentPageOutput.class,
                    name = "printCurrentPage"),
            @Type(value = AddEventCommand.AddEventOutput.class, name = "addEvent"),
            @Type(value = AddMerchCommand.AddMerchOutput.class, name = "addMerch"),
            @Type(value = GetAllUsersCommand.GetAllUsersOutput.class, name = "getAllUsers"),
            @Type(value = DeleteUserCommand.DeleteUserOutput.class, name = "deleteUser"),
            @Type(value = AddPodcastCommand.AddPodcastOutput.class, name = "addPodcast"),
            @Type(value = ShowPodcastsCommand.ShowPodcastsOutput.class, name = "showPodcasts"),
            @Type(value = RemoveAnnouncementCommand.RemoveAnnouncementOutput.class,
                    name = "removeAnnouncement"),
            @Type(value = AddAnnouncementCommand.AddAnnouncementOutput.class,
                    name = "addAnnouncement"),
            @Type(value = RemoveEventCommand.RemoveEventOutput.class, name = "removeEvent"),
            @Type(value = RemoveMerchCommand.RemoveMerchOutput.class, name = "removeMerch"),
            @Type(value = RemovePodcastCommand.RemovePodcastOutput.class, name = "removePodcast"),
            @Type(value = RemoveAlbumCommand.RemoveAlbumOutput.class, name = "removeAlbum"),
            @Type(value = ChangePageCommand.ChangePageOutput.class, name = "changePage"),
            @Type(value = WrappedCommand.WrappedOutput.class, name = "wrapped"),
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
