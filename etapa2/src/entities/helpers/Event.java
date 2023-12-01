package entities.helpers;

import lombok.Getter;

@Getter
public final class Event {
    private final String name;
    private final String description;
    private final String date;

    public Event(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    @Override
    public String toString() {
        return name + " - " + date + ":\\n\\t" + description;
    }
}
