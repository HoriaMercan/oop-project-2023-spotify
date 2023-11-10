package entities;

import entities.audioCollections.AudioCollection;

import java.util.List;

public class UserPlayer {
	public UserPlayer() {}
	String lastSelected = "", typeSearched, typeLoaded;
	private List<String> lastSearched;
	AudioFileSelector Selector;
	Integer lastUpdatedTime;
	Integer currentAudioFileTime;
	AudioCollection context;
	private boolean isPaused;

	public void updatePlayer(Integer currentTime) {
		if (this.isPaused) {
			return;
		}

		Integer deltaTime = currentTime - lastUpdatedTime;
		if (currentAudioFileTime + deltaTime >= Selector.current().getDuration()) {
			deltaTime -= (Selector.current().getDuration() - currentAudioFileTime);
			currentAudioFileTime = 0;
			Selector.next();
		}
		while (!Selector.end() && deltaTime >= Selector.current().getDuration()) {
			deltaTime -= Selector.current().getDuration();
			Selector.next();
		}
		currentAudioFileTime = deltaTime;

	}

}

