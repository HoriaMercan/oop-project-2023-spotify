package entities.audioFileSelector;

import entities.audioFiles.AudioFile;

public class AudioFileSelector {
	protected AudioFileSelectorCurrent basic;
	protected AudioFileSelectorEnd end;
	protected AudioFileSelectorNext next;
	protected AudioFileSelectorOutOfBound outOfBound;

	AudioFileSelector(AudioFileSelectorCurrent basic,
					  AudioFileSelectorNext next,
					  AudioFileSelectorEnd end) {
		this.basic = basic;
		this.next = next;
		this.end = end;
	}

	public AudioFileSelector(AudioFileSelectorCurrent basic,
							 AudioFileSelectorNext next,
							 AudioFileSelectorEnd end,
							 AudioFileSelectorOutOfBound outOfBound) {
		this(basic, next, end);
		this.outOfBound = outOfBound;
	}

	public AudioFileSelector(AudioFileSelectorOutOfBound outOfBound) {
		this.outOfBound = outOfBound;
	}

	public AudioFile current() {
		if (end()) {
			return outOfBound.outOfBound();
		}
		return basic.__current();
	}

	public boolean end() {
		return end.__end();
	}
	public void next() {
		next.__next();
		if (end.__end()) {
			outOfBound.nextWhenEnded();
		}
	}

	public void setBasic(AudioFileSelectorCurrent basic) {
		this.basic = basic;
	}

	public void setEnd(AudioFileSelectorEnd end) {
		this.end = end;
	}
	public void setNext(AudioFileSelectorNext next) {
		this.next = next;
	}
}
