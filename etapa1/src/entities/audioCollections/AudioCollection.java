package entities.audioCollections;

public abstract class AudioCollection {
    protected String name;
    protected String owner;

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

}
