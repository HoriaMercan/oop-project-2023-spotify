package entities.audioFiles;

public abstract class AudioFile {
    protected String name;
    protected Integer duration;

    public AudioFile(final String name, final Integer duration) {
        this.name = name;
        this.duration = duration;
    }

    public AudioFile() {

    }

    public final String getName() {
        return this.name;
    }

    public final Integer getDuration() {
        return this.duration;
    }
}
