package entities.audioFileSelector;

import entities.audioFiles.AudioFile;

public interface AudioFileSelectorOutOfBound {
	AudioFile outOfBound();

	void nextWhenEnded();
}
