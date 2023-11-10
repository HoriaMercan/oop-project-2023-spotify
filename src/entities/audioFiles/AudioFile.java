package entities.audioFiles;

public abstract class AudioFile {
	protected String name;
	protected Integer duration;
	public AudioFile(String name, Integer duration) {
		this.name = name;
		this.duration = duration;
	}

	public AudioFile() {

	}

	public String getName() {
		return this.name;
	}
	public Integer getDuration() {
		return this.duration;
	}
}
