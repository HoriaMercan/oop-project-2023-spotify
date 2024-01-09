package entities.audioFiles;

import lombok.Setter;

public abstract class AudioFile {
    protected String name;
    protected Integer duration;

    @Setter
    protected String creator;

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

    public final String getCreator() {
        return creator;
    }
}
