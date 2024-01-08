package entities.helpers;

import lombok.Getter;

@Getter
public final class Notification {
    private final String name;
    private final String description;

    public Notification(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
