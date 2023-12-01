package entities.audioFileSelector;

import entities.audioFiles.AudioFile;


/**
 * Primitive getting current element class
 */
public interface AudioFileSelectorCurrent {

    /**
     * @return The current audio file which is active in player
     */
    AudioFile current();
}
