package entities.users;

import entities.audioCollections.Album;
import entities.audioCollections.AudioCollection;
import entities.audioFiles.AudioFile;
import entities.helpers.Event;
import entities.helpers.Merch;
import lombok.Getter;
import lombok.Setter;
import page_system.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Artist extends AbstractUser implements Pageable, ContentCreator {
    private final List<Album> albums = new ArrayList<>();
    private final List<Merch> merches = new ArrayList<>();
    private final List<Event> events = new ArrayList<>();

    public Artist(String username, String city, Integer age) {
        super(username, city, age);
        this.userType = UserType.ARTIST;
    }

    public boolean hasAlbum(String albumName) {
        List<Album> ans = albums.stream()
                .filter(album1 -> album1.getName().equals(albumName)).toList();

        return !ans.isEmpty();
    }

    public Event hasEvent(String eventName) {
        List<Event> event = events.stream()
                .filter(e -> e.getName().equals(eventName)).toList();

        return event.isEmpty() ? null : event.get(0);
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public boolean removeAlbum(Album album) {
        return albums.remove(album);
    }

    @Override
    public String getPageContent() {
        StringBuilder stringBuilder = new StringBuilder("Albums:\n\t[");

        if (!albums.isEmpty()){
            for (Album album : albums) {
                stringBuilder.append(album.toString()).append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append("]\n\nMerch:\n\t[");

       if (!merches.isEmpty()) {
            for (Merch merch : merches) {
                stringBuilder.append(merch).append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append("]\n\nEvents:\n\t[");

        if (!events.isEmpty()){
            for (Event event : events) {
                stringBuilder.append(event).append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    @Override
    public List<? extends AudioCollection> getContent() {
        return this.albums;
    }
}
