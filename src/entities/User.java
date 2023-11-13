package entities;

import entities.audioFileSelector.*;
import entities.audioFiles.AudioFile;
import fileio.input.UserInput;

import java.util.*;

public class User {
	final String username;
	final String city;
	final Integer age;

	private final HashMap<Integer, String> userPlaylists = new HashMap<>();

	public boolean isPlaylistInUserList(String playlistName) {
		return userPlaylists.containsValue(playlistName);
	}

	public void addPlaylistInUserList(String playlistName) {
		userPlaylists.put(userPlaylists.size() + 1, playlistName);
	}

	public boolean isPlaylistIDInUserList(Integer id) {return userPlaylists.containsKey(id);}
	public String getPlaylistFromID(Integer id) {
		return userPlaylists.get(id);
	}
	public User(String username, String city, Integer age) {
		this.username = username;
		this.city = city;
		this.age = age;
	}

	public class UserPlayer {

		// this retain the episode
		public HashMap<String, Integer> podcastRemainderTime = new HashMap<>();
		public HashMap<String, Integer> podcastRemainderEpisode = new HashMap<>();

		protected UserPlayer() {}
		public List<String> getLastSearched() {
			return lastSearched;
		}

		public void setLastSearched(List<String> lastSearched) {
			this.lastSearched = lastSearched;
		}

		public String getLastSelected() {
			return lastSelected;
		}

		public void setLastSelected(String lastSelected) {
			this.lastSelected = lastSelected;
		}

		public String getTypeSearched() {
			return typeSearched;
		}

		public void setTypeSearched(String typeSearched) {
			this.typeSearched = typeSearched;
		}

		public void loadPlayer(Integer timestamp) {
			lastSelected = "";
			saveContext(timestamp);
			this.typeLoaded = this.typeSearched;
			this.isPaused = false;
			this.lastUpdatedTime = timestamp;

			if (this.typeLoaded.equals("podcast")) {
				if (podcastRemainderTime.containsKey(playedPodcastName)) {
					currentAudioFileTime = podcastRemainderTime.get(playedPodcastName);
					index = podcastRemainderEpisode.get(playedPodcastName);
				}
				else {
					currentAudioFileTime = 0;
					index = 0;
				}
			} else {currentAudioFileTime = 0; index = 0;}

		}

		private String playedPodcastName = "";

		public void setPlayedPodcastName(String playedPodcastName) {
			this.playedPodcastName = playedPodcastName;
		}

		public void saveContext(Integer timestamp) {
			updatePlayer(timestamp);
			if (this.typeLoaded.equals("podcast")) {
				podcastRemainderTime.put(playedPodcastName, currentAudioFileTime);
				podcastRemainderEpisode.put(playedPodcastName, index);
			}
		}
		public void unsetContext(Integer timestamp) {
			saveContext(timestamp);

			this.typeLoaded = "";
//			this.isPaused = true;
//			this.repeatStatus = 0;
//			this.currentAudioFileTime = Selector.current().getDuration();
		}

		public String playPause(Integer currentTime) {
			if (!this.isPaused) {
				updatePlayer(currentTime);
				this.isPaused = !this.isPaused;
				return "Playback paused successfully.";
			}
			else {
				this.lastUpdatedTime = currentTime;
				this.playTime = currentTime;
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

		private List<String> lastSearched;
		private String typeSearched;
		private String lastSelected = "";

		private String typeLoaded = "";
		private List<? extends AudioFile> context;
		private Integer playTime;
		private boolean isPaused;
		
		private boolean isShuffle;
		private Integer repeatStatus;

		boolean repeatOnce = false;

		private void changeRepeatToNoRepeat() {
			this.Selector.setEnd(
					() -> UserPlayer.this.index >= UserPlayer.this.context.size()
			);
			this.Selector.setNext(
					() -> UserPlayer.this.index += 1
			);
		}

		private void changeRepeatToRepeatAll() {
			Selector.setEnd(
					() -> false
			);
			Selector.setNext(
					() -> UserPlayer.this.index = (UserPlayer.this.index + 1) % UserPlayer.this.context.size()
			);
		}

		private void changeRepeatToRepeatCurrentSong() {
			UserPlayer.this.repeatOnce = false;
			Selector.setEnd(
					() -> UserPlayer.this.index >= UserPlayer.this.context.size()
			);
			Selector.setNext(
					new AudioFileSelectorNext() {
						@Override
						public void __next() {
							if (!UserPlayer.this.repeatOnce) {
								UserPlayer.this.repeatOnce = true;
								UserPlayer.this.repeatStatus = 0;
							}
							else {
								UserPlayer.this.index += 1;

							}
						}
					}
			);
		}

		private void changeRepeatToRepeatInfinite() {
			Selector.setEnd(
					() -> false
			);
			Selector.setNext(
					() -> {}
			);
		}

		public String changeRepeatStatus() {
			UserPlayer.this.repeatStatus = (UserPlayer.this.repeatStatus + 1) % 3;
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

		public String getTypeLoaded(User user) {
			return user.getPlayer().getTypeLoaded();
		}

		public int index;
		private ArrayList<Integer> indexArrays;

		public void setContext(List<? extends AudioFile> context, Integer timestamp) {
			unsetContext(timestamp);
			this.context = context;
			indexArrays = new ArrayList<>();
			for (int i = 0; i < this.context.size(); i++) {
				indexArrays.add(i);
			}
			this.Selector = new AudioFileSelector(
					() -> UserPlayer.this.context.get(indexArrays.get(UserPlayer.this.index)),
					() -> UserPlayer.this.index += 1,
					() -> UserPlayer.this.index >= UserPlayer.this.context.size(),
					outOfBound
			);

			UserPlayer.this.repeatStatus = 0;
		}

		public void doShuffle(Integer seed) {
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

		public void runForward() {
			if (getRemainedTime() > 90) {
				currentAudioFileTime += 90;
			} else {
				currentAudioFileTime = 0;
				Selector.next();
			}
		}

		public void runBackward() {
			if (currentAudioFileTime >= 90) {
				currentAudioFileTime -= 90;
			} else {
				currentAudioFileTime = 0;
			}
		}

		public void runNext() {
			Selector.next();
			currentAudioFileTime = 0;
		}

		public void runPrev() {
			if (currentAudioFileTime > 1) {
				currentAudioFileTime = 0;
				return;
			}
			if (this.index == 0)
				return;

			this.index -= 1;
		}
		public AudioFile getCurrentPlayed() {
			return Selector.current();
		}

		public String getCurrentPlayedName() {
			if (Selector.end())
				return "";
			return Selector.current().getName();
		}
		public Integer getRemainedTime() {
			return Selector.current().getDuration() - currentAudioFileTime;
		}
		final AudioFileSelectorOutOfBound outOfBound = new AudioFileSelectorOutOfBound() {
			@Override
			public AudioFile outOfBound() {
				return UserPlayer.this.context.get(context.size()-1);
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

		AudioFileSelector Selector;
		Integer lastUpdatedTime;
		Integer currentAudioFileTime = 0;
		public void updatePlayer(Integer currentTime) {
			if (typeLoaded.isEmpty())
				return;
			if (lastUpdatedTime == null) {
				return;
			}
			if (this.isPaused) {
				lastUpdatedTime = currentTime;
				return;
			}

			Integer deltaTime = currentTime - lastUpdatedTime + currentAudioFileTime;

			if (deltaTime >= Selector.current().getDuration()) {
				deltaTime -= Selector.current().getDuration();
				Selector.next();
			}
			while (!Selector.end() && deltaTime >= Selector.current().getDuration()) {
				deltaTime -= Selector.current().getDuration();
				Selector.next();
			}
			currentAudioFileTime = deltaTime;
			lastUpdatedTime = currentTime;
			if (Selector.end()) {
				currentAudioFileTime = Selector.current().getDuration();
			}
			if (typeLoaded.equals("podcast")) {
//				this.podcastsPlayed.put()
				this.podcastRemainderEpisode.put(this.playedPodcastName,index);
				this.podcastRemainderTime.put(this.playedPodcastName, currentAudioFileTime);
			}


		}
	}

	public UserPlayer getPlayer() {
		return player;
	}

	protected final UserPlayer player = new UserPlayer();

	public User(UserInput input) {
		this(input.getUsername(), input.getCity(), input.getAge());
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

}
