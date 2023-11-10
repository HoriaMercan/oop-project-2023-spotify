package entities;

import entities.audioFiles.AudioFile;
import entities.audioFiles.PodcastEpisode;
import fileio.input.UserInput;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class User {
	final String username;
	final String city;
	final Integer age;


	public User(String username, String city, Integer age) {
		this.username = username;
		this.city = city;
		this.age = age;
	}

	public class UserPlayer1 {

		protected UserPlayer1() {}
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
			this.typeLoaded = this.typeSearched;
			this.isPaused = false;
			this.lastUpdatedTime = timestamp;
		}

		public String playPause(Integer currentTime) {
			if (!this.isPaused) {
				updatePlayer(currentTime);
				this.isPaused = !this.isPaused;
				return "Playback paused successfully.";
			}
			else {
				lastUpdatedTime = currentTime;
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
				case "playlist" -> switch (repeatStatus) {
					case 0 -> "No Repeat";
					case 1 -> "Repeat All";
					case 2 -> "Repeat Current Song";
					default -> "";
				};
				default -> switch (repeatStatus) {
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

		public String getTypeLoaded() {
			return typeLoaded;
		}

		public int index;
		public void setContext(List<? extends AudioFile> context) {
			this.context = context;
			Selector = new AudioFileSelector() {

				@Override
				public boolean end() {
					return index >= context.size();
				}

				@Override
				public AudioFile current() {
					if (end()) {
						return context.get(context.size()-1);
					}
					return context.get(index);
				}

				@Override
				public Integer currentIndex() {
					return index;
				}

				@Override
				public void next() {
					index += 1;
					if (this.end()) {
						typeLoaded = "";
						index = context.size();
						currentAudioFileTime = context.get(index - 1).getDuration();
						isPaused = true;
					}
				}
			};
			repeatStatus = 0;
		}

		public AudioFile getCurrentPlayed() {

			return Selector.current();
		}
		public Integer getRemainedTime() {
			return Selector.current().getDuration() - currentAudioFileTime;
		}
		AudioFileSelector Selector;
		Integer lastUpdatedTime;
		Integer currentAudioFileTime = 0;
		public void updatePlayer(Integer currentTime) {
			if (this.isPaused) {
				return;
			}

			Integer deltaTime = currentTime - lastUpdatedTime + currentAudioFileTime;
			if (deltaTime >= Selector.current().getDuration()) {
				deltaTime -= (Selector.current().getDuration() - currentAudioFileTime);
				currentAudioFileTime = 0;
				Selector.next();
			}
			while (!Selector.end() && deltaTime >= Selector.current().getDuration()) {
				deltaTime -= Selector.current().getDuration();
				Selector.next();
			}
			currentAudioFileTime = deltaTime;
			lastUpdatedTime = currentTime;
			if (typeLoaded.equals("podcast")) {
//				this.podcastsPlayed.put()
			}
			if (Selector.end()) {
				currentAudioFileTime = Selector.current().getDuration();
			}

		}
	}

	public UserPlayer1 getPlayer() {
		return player;
	}

	protected final UserPlayer1 player = new UserPlayer1();

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
