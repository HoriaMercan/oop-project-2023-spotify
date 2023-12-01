package entities.audioFileSelector;

import entities.audioFiles.AudioFile;

/**
 * Handles selector's choices when the cursor would not fit in the player's bound
 */
public interface AudioFileSelectorOutOfBound {
    /**
     * @return a file which represents the last playable available in the player
     */
    AudioFile outOfBound();

    /**
     * Handles how to move to next element when it does not exists
     */
    void nextWhenEnded();
}
