package entities.audioCollections;

import entities.audioFiles.AudioFile;

import java.util.List;

public abstract class AudioCollection {
    protected String name;
    protected String owner;

    public AudioCollection() {
    }

    public AudioCollection(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getOwner() {
        return owner;
    }

    public final void setOwner(final String owner) {
        this.owner = owner;
    }

    public List<? extends AudioFile> getAudioFiles() {
        return null;
    }

}
