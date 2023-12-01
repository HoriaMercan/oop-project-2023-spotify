package entities.helpers;

import lombok.Getter;
import lombok.Setter;

@Getter
public final class Merch {
    private final String name;
    private final String description;
    private final Integer price;

    public Merch(final String name, final String description, final Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " - " + price + ":\n\t" + description;
    }
}
