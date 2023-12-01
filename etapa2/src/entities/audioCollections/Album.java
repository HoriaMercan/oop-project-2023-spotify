package entities.audioCollections;

import entities.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public final class Album extends AudioCollection {
    private List<Song> songs = new ArrayList<>();
    private int releaseYear;
    private String description;

    private Set<String> usersListening = new HashSet<>();

    public Album(){}
    public Album(final String name, final String owner, final int releaseYear,
                 final String description) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
    }

    public Album(final String name, final String owner, final int releaseYear,
                 final String description, final List<Song> songs) {
        this(name, owner, releaseYear, description);
        this.songs = songs;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
