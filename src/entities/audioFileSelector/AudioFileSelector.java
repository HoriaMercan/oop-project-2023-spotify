package entities.audioFileSelector;

import entities.audioFiles.AudioFile;

/**
 * Class AudioFileSelector offers the methods necessary to operate through
 * audio files by puzzling every primitive method and building the formal API commands
 * that can be changes easily (an implementation with lambda expressions might be
 * very useful for a big part of changes)
 */
public class AudioFileSelector {
    protected AudioFileSelectorCurrent basic;
    protected AudioFileSelectorEnd end;
    protected AudioFileSelectorNext next;
    protected AudioFileSelectorOutOfBound outOfBound;

    AudioFileSelector(final AudioFileSelectorCurrent basic,
                      final AudioFileSelectorNext next,
                      final AudioFileSelectorEnd end) {
        this.basic = basic;
        this.next = next;
        this.end = end;
    }

    public AudioFileSelector(final AudioFileSelectorCurrent basic,
                             final AudioFileSelectorNext next,
                             final AudioFileSelectorEnd end,
                             final AudioFileSelectorOutOfBound outOfBound) {
        this(basic, next, end);
        this.outOfBound = outOfBound;
    }

    public AudioFileSelector(final AudioFileSelectorOutOfBound outOfBound) {
        this.outOfBound = outOfBound;
    }

    /**
     * @return the current AudioFile object that is shown by player
     */
    public AudioFile current() {
        if (end()) {
            return outOfBound.outOfBound();
        }
        return basic.current();
    }

    /**
     * @return Player got to the end
     */
    public boolean end() {
        return end.end();
    }

    /**
     * The selector moves to the next song, by considering the active properties of
     * the player
     */
    public void next() {
        next.next();
        if (end.end()) {
            outOfBound.nextWhenEnded();
        }
    }

    public final void setBasic(final AudioFileSelectorCurrent basic) {
        this.basic = basic;
    }

    public final void setEnd(final AudioFileSelectorEnd end) {
        this.end = end;
    }

    public final void setNext(final AudioFileSelectorNext next) {
        this.next = next;
    }
}
