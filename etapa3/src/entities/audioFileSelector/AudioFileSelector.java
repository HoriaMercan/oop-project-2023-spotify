package entities.audioFileSelector;

import databases.MyDatabase;
import entities.audioFiles.AudioFile;
import entities.users.User;
import entities.wrapper.OneListen;
import entities.wrapper.statistics.WrapperStatistics;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

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

    protected AudioFileSender send;//ToDo: Complete here to
    // send one listen to the wrapper

    protected User self;

    @Getter
    protected boolean adActive = false;

    @Setter
    protected boolean nextIsAd = false;

    AudioFileSelector(final User user,
                      final AudioFileSelectorCurrent basic,
                      final AudioFileSelectorNext next,
                      final AudioFileSelectorEnd end) {
        this.self = user;
        this.basic = basic;
        this.next = next;
        this.end = end;
    }

    public AudioFileSelector(final User user,
                             final AudioFileSelectorCurrent basic,
                             final AudioFileSelectorNext next,
                             final AudioFileSelectorEnd end,
                             final AudioFileSelectorOutOfBound outOfBound) {
        this(user, basic, next, end);
        this.outOfBound = outOfBound;
        send = null;
    }

    public AudioFileSelector(final User user,
                             final AudioFileSelectorCurrent basic,
                             final AudioFileSelectorNext next,
                             final AudioFileSelectorEnd end,
                             final AudioFileSelectorOutOfBound outOfBound,
                             final AudioFileSender send) {
        this(user, basic, next, end, outOfBound);
        this.send = send;
    }

    public AudioFileSelector(final AudioFileSelectorOutOfBound outOfBound) {
        this.outOfBound = outOfBound;
    }

    /**
     * @return the current AudioFile object that is shown by player
     */
    public AudioFile current() {
        if (adActive)
            return MyDatabase.getInstance().getAdBreak();
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
        if (nextIsAd) {
            nextIsAd = false;
            adActive = true;

            self.getPayment().payToArtists();
            return;
        }
        if (current().equals(MyDatabase.getInstance().getAdBreak())) {
            adActive = false;
        }

        next.next();
        if (end.end()) {
            outOfBound.nextWhenEnded();
            return;
        }
        if (send != null) {
            send.send();
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

    public final void unsetAds() {
        this.adActive = false;
        this.nextIsAd = false;
        this.self.getPayment().setTotalMoney(0.0);
    }

}
