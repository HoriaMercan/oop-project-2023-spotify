package entities;

import entities.audioFileSelector.AudioFileSelector;
import entities.audioFileSelector.AudioFileSelectorNext;
import entities.audioFileSelector.AudioFileSelectorOutOfBound;
import entities.audioFiles.AudioFile;
import fileio.input.UserInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public final class User {
    private final UserPlayer player = new UserPlayer();
    private final String username;
    private final String city;
    private final Integer age;
    private final HashMap<Integer, String> userPlaylists = new HashMap<>();

    public User(final String username, final String city, final Integer age) {
        this.username = username;
        this.city = city;
        this.age = age;
    }

    public User(final UserInput input) {
        this(input.getUsername(), input.getCity(), input.getAge());
    }

    /**
     * @param playlistName playlist name
     * @return True if the given playlistName already exists in the user's associated
     * list of playlists
     */
    public boolean isPlaylistInUserList(final String playlistName) {
        return userPlaylists.containsValue(playlistName);
    }

    /**
     * Creates a new playlist associated with the user
     *
     * @param playlistName A new playlist to be created
     */
    public void addPlaylistInUserList(final String playlistName) {
        userPlaylists.put(userPlaylists.size() + 1, playlistName);
    }

    /**
     * @param id playlistID
     * @return True if the given playlistID already exists in the user's associated
     * *          list of playlists
     */
    public boolean isPlaylistIDInUserList(final Integer id) {
        return userPlaylists.containsKey(id);
    }

    /**
     * @param id a playlistID
     * @return the playlist name associated with the playlist id
     */
    public String getPlaylistFromID(final Integer id) {
        return userPlaylists.get(id);
    }

    public UserPlayer getPlayer() {
        return player;
    }

    public String getUsername() {
        return username;
    }

    public String getCity() {
        return city;
    }

    public Integer getAge() {
        return age;
    }

    public final class UserPlayer {

        // this retain the episode
        private final HashMap<String, Integer> podcastRemainderTime = new HashMap<>();
        private final HashMap<String, Integer> podcastRemainderEpisode = new HashMap<>();
        private final int repeatStatusNo = 3;
        private final int timeThreshold = 90;
        private int index;
        private boolean repeatOnce = false;
        private AudioFileSelector selector;
        private Integer lastUpdatedTime;
        private Integer currentAudioFileTime = 0;
        private String playedPodcastName = "";
        private List<String> lastSearched;
        private String typeSearched;
        private String lastSelected = "";
        private String typeLoaded = "";
        private List<? extends AudioFile> context;
        private boolean isPaused;
        private boolean isShuffle;
        private Integer repeatStatus;
        private final AudioFileSelectorOutOfBound outOfBound = new AudioFileSelectorOutOfBound() {
            @Override
            public AudioFile outOfBound() {
                return UserPlayer.this.context.get(context.size() - 1);
            }

            @Override
            public void nextWhenEnded() {
                UserPlayer.this.typeLoaded = "";
                UserPlayer.this.index = UserPlayer.this.context.size();
                UserPlayer.this.currentAudioFileTime =
                        UserPlayer.this.context.get(index - 1).getDuration();
                UserPlayer.this.isPaused = true;
                UserPlayer.this.repeatStatus = 0;
            }
        };
        private ArrayList<Integer> indexArrays;

        private UserPlayer() {
        }

        public List<String> getLastSearched() {
            return lastSearched;
        }

        public void setLastSearched(final List<String> lastSearched) {
            this.lastSearched = lastSearched;
        }

        public String getLastSelected() {
            return lastSelected;
        }

        public void setLastSelected(final String lastSelected) {
            this.lastSelected = lastSelected;
        }

        public String getTypeSearched() {
            return typeSearched;
        }

        public void setTypeSearched(final String typeSearched) {
            this.typeSearched = typeSearched;
        }

        /**
         * This function load in a player an audio collection or file which has been previously
         * selected and saves the actual state if necessary
         *
         * @param timestamp a time at which the action is performed
         */
        public void loadPlayer(final Integer timestamp) {
            lastSelected = "";
            saveContext(timestamp);
            this.typeLoaded = this.typeSearched;
            this.isPaused = false;
            this.lastUpdatedTime = timestamp;

            if (this.typeLoaded.equals("podcast")) {
                if (podcastRemainderTime.containsKey(playedPodcastName)) {
                    currentAudioFileTime = podcastRemainderTime.get(playedPodcastName);
                    index = podcastRemainderEpisode.get(playedPodcastName);
                } else {
                    currentAudioFileTime = 0;
                    index = 0;
                }
            } else {
                currentAudioFileTime = 0;
                index = 0;
            }

        }

        public void setPlayedPodcastName(final String playedPodcastName) {
            this.playedPodcastName = playedPodcastName;
        }

        /**
         * This function updates the context of a player and saves
         *
         * @param timestamp a time at which the action is performed
         */
        public void saveContext(final Integer timestamp) {
            updatePlayer(timestamp);
            if (this.typeLoaded.equals("podcast")) {
                podcastRemainderTime.put(playedPodcastName, currentAudioFileTime);
                podcastRemainderEpisode.put(playedPodcastName, index);
            }
        }

        /**
         * This function saves current context and then free the player
         *
         * @param timestamp time of action performed
         */
        public void unsetContext(final Integer timestamp) {
            saveContext(timestamp);

            this.typeLoaded = "";
        }

        /**
         * The function pause/play a player, and it handles to update the player
         *
         * @param currentTime tme
         * @return message resulted from the action
         */
        public String playPause(final Integer currentTime) {
            if (!this.isPaused) {
                updatePlayer(currentTime);
                this.isPaused = !this.isPaused;
                return "Playback paused successfully.";
            } else {
                this.lastUpdatedTime = currentTime;
                this.isPaused = !this.isPaused;
                return "Playback resumed successfully.";
            }

        }

        public boolean isPaused() {
            return isPaused;
        }

        public boolean isShuffle() {
            return isShuffle;
        }

        public String getRepeatStatus() {
            return switch (typeLoaded) {
                case "playlist" -> switch (UserPlayer.this.repeatStatus) {
                    case 0 -> "No Repeat";
                    case 1 -> "Repeat All";
                    case 2 -> "Repeat Current Song";
                    default -> "";
                };
                default -> switch (UserPlayer.this.repeatStatus) {
                    case 0 -> "No Repeat";
                    case 1 -> "Repeat Once";
                    case 2 -> "Repeat Infinite";
                    default -> "";
                };
            };
        }

        private void changeRepeatToNoRepeat() {
            this.selector.setEnd(() -> UserPlayer.this.index >= UserPlayer.this.context.size());
            this.selector.setNext(() -> UserPlayer.this.index += 1);
        }

        private void changeRepeatToRepeatAll() {
            selector.setEnd(() -> false);
            selector.setNext(() -> UserPlayer.this.index = (UserPlayer.this.index + 1)
                    % UserPlayer.this.context.size());
        }

        private void changeRepeatToRepeatCurrentSong() {
            UserPlayer.this.repeatOnce = false;
            selector.setEnd(() -> UserPlayer.this.index >= UserPlayer.this.context.size());
            selector.setNext(new AudioFileSelectorNext() {
                @Override
                public void next() {
                    if (!UserPlayer.this.repeatOnce) {
                        UserPlayer.this.repeatOnce = true;
                        UserPlayer.this.repeatStatus = 0;
                    } else {
                        UserPlayer.this.index += 1;

                    }
                }
            });
        }

        private void changeRepeatToRepeatInfinite() {
            selector.setEnd(() -> false);
            selector.setNext(() -> {
            });
        }

        /**
         * Function for internally changing the selector objects pieces in order to globally
         * change the repeat status
         *
         * @return the message resulted from changing the status
         */
        public String changeRepeatStatus() {
            UserPlayer.this.repeatStatus = (UserPlayer.this.repeatStatus + 1) % repeatStatusNo;
            if (typeLoaded.equals("playlist")) {
                return switch (UserPlayer.this.repeatStatus) {
                    case 0 -> {
                        changeRepeatToNoRepeat();
                        yield "no repeat";
                    }
                    case 1 -> {
                        changeRepeatToRepeatAll();
                        yield "repeat all";
                    }
                    case 2 -> {
                        changeRepeatToRepeatInfinite();
                        yield "repeat current song";
                    }
                    default -> "";
                };
            }
            return switch (UserPlayer.this.repeatStatus) {
                case 0 -> {
                    changeRepeatToNoRepeat();
                    yield "no repeat";
                }
                case 1 -> {
                    changeRepeatToRepeatCurrentSong();
                    yield "repeat once";
                }
                case 2 -> {
                    changeRepeatToRepeatInfinite();
                    yield "repeat infinite";
                }
                default -> "";
            };
        }

        public String getTypeLoaded() {
            return typeLoaded;
        }

        /**
         * This function set a new contextIn (selector type/ audio files) when something is loaded
         *
         * @param contextIn   List of audio files to be loaded in the player
         * @param timestamp time
         */
        public void setContext(final List<? extends AudioFile> contextIn, final Integer timestamp) {
            unsetContext(timestamp);
            this.context = contextIn;
            indexArrays = new ArrayList<>();
            for (int i = 0; i < this.context.size(); i++) {
                indexArrays.add(i);
            }
            this.selector = new AudioFileSelector(() -> UserPlayer.this.context
                    .get(indexArrays.get(UserPlayer.this.index)),
                    () -> UserPlayer.this.index += 1,
                    () -> UserPlayer.this.index >= UserPlayer.this.context.size(), outOfBound);

            UserPlayer.this.repeatStatus = 0;
        }

        /**
         * Shuffles the playlist \w given seed
         *
         * @param seed random seed
         */
        public void doShuffle(final Integer seed) {
            this.isShuffle = true;
            int actualIndex = indexArrays.get(this.index);
            this.indexArrays = new ArrayList<>();
            for (int i = 0; i < this.context.size(); i++) {
                indexArrays.add(i);
            }
            Collections.shuffle(this.indexArrays, new Random(seed));
            for (int i = 0; i < this.context.size(); i++) {
                if (this.indexArrays.get(i) == actualIndex) {
                    this.index = i;
                    break;
                }
            }
        }

        /**
         * Make the songs be played in the linear order, not shuffled
         */
        public void undoShuffle() {
            this.isShuffle = false;
            int actualIndex = indexArrays.get(this.index);
            this.indexArrays = new ArrayList<>();
            for (int i = 0; i < this.context.size(); i++) {
                indexArrays.add(i);
            }

            for (int i = 0; i < this.context.size(); i++) {
                if (this.indexArrays.get(i) == actualIndex) {
                    this.index = i;
                    break;
                }
            }
        }

        /**
         * Do forward on player
         */
        public void runForward() {
            if (getRemainedTime() > timeThreshold) {
                currentAudioFileTime += timeThreshold;
            } else {
                currentAudioFileTime = 0;
                selector.next();
            }
        }

        /**
         * Do backward on player
         */
        public void runBackward() {
            if (currentAudioFileTime >= timeThreshold) {
                currentAudioFileTime -= timeThreshold;
            } else {
                currentAudioFileTime = 0;
            }
        }

        /**
         * Do next
         *
         * @param timestamp time
         */
        public void runNext(final Integer timestamp) {
            updatePlayer(timestamp);
            if (selector.end()) {
                return;
            }

            selector.next();

            currentAudioFileTime = 0;

            if (this.isPaused) {
                this.isPaused = false;
            }

        }

        /**
         * Do prev
         */
        public void runPrev() {
            if (this.isPaused) {
                this.isPaused = false;
            }
            if (currentAudioFileTime > 1) {
                currentAudioFileTime = 0;
                return;
            }
            if (this.index == 0) {
                return;
            }

            this.index -= 1;
        }

        public AudioFile getCurrentPlayed() {
            return selector.current();
        }

        /**
         * @return The name of the current audio file in player
         */
        public String getCurrentPlayedName() {
            return selector.current().getName();
        }

        /**
         * @return The remained time until current audio file finishes
         */
        public Integer getRemainedTime() {
            return selector.current().getDuration() - currentAudioFileTime;
        }

        /**
         * @param currentTime time to which a player is updated
         */
        public void updatePlayer(final Integer currentTime) {
            if (typeLoaded.isEmpty()) {
                return;
            }
            if (lastUpdatedTime == null) {
                return;
            }
            if (this.isPaused) {
                lastUpdatedTime = currentTime;
                return;
            }

            Integer deltaTime = currentTime - lastUpdatedTime + currentAudioFileTime;

            if (deltaTime >= selector.current().getDuration()) {
                deltaTime -= selector.current().getDuration();
                selector.next();
            }
            while (!selector.end() && deltaTime >= selector.current().getDuration()) {
                deltaTime -= selector.current().getDuration();
                selector.next();
            }
            currentAudioFileTime = deltaTime;
            lastUpdatedTime = currentTime;
            if (selector.end()) {
                currentAudioFileTime = selector.current().getDuration();
            }
            if (typeLoaded.equals("podcast")) {
                this.podcastRemainderEpisode.put(this.playedPodcastName, index);
                this.podcastRemainderTime.put(this.playedPodcastName, currentAudioFileTime);
            }


        }
    }
}
