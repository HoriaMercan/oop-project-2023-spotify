package entities.helpers;

import lombok.Getter;

@Getter
public final class Event {
    private final String name;
    private final String description;
    private final String date;

    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    @Override
    public String toString() {
        return name + " - " + date + ":\n\t" + description;
    }
}
