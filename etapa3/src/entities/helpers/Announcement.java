package entities.helpers;

import lombok.Getter;

@Getter
public final class Announcement {
    private final String name;
    private final String description;

    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return name + ":\n\t" + description + "\n";
    }
}
