package entities.users;

import entities.audioCollections.Album;
import entities.audioCollections.AudioCollection;
import entities.helpers.Event;
import entities.helpers.Merch;
import entities.wrapper.statistics.ArtistWrapperStatistics;
import lombok.Getter;
import lombok.Setter;
import pagesystem.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class Artist extends AbstractUser implements Pageable, ContentCreator {
    private final List<Album> albums = new ArrayList<>();
    private final List<Merch> merches = new ArrayList<>();
    private final List<Event> events = new ArrayList<>();

    public Artist(final String username, final String city, final Integer age) {
        super(username, city, age);
        this.userType = UserType.ARTIST;
        wrapperStatistics = new ArtistWrapperStatistics();
    }

    /**
     * @param albumName
     * @return verify whether the artist has an album \w given name
     */
    public boolean hasAlbum(final String albumName) {
        List<Album> ans = albums.stream()
                .filter(album1 -> album1.getName().equals(albumName)).toList();

        return !ans.isEmpty();
    }

    /**
     * @param eventName
     * @return verifies whether the artist has an event \w given name
     */
    public Event hasEvent(final String eventName) {
        List<Event> event = events.stream()
                .filter(e -> e.getName().equals(eventName)).toList();

        return event.isEmpty() ? null : event.get(0);
    }

    /**
     * @param album
     */
    public void addAlbum(final Album album) {
        albums.add(album);
    }

    /**
     * @param album
     * @return remove an album if it exists and return true, otherwise false
     */
    public boolean removeAlbum(final Album album) {
        return albums.remove(album);
    }

    @Override
    public String getPageContent() {
        StringBuilder stringBuilder = new StringBuilder("Albums:\n\t[");

        if (!albums.isEmpty()) {
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

        if (!events.isEmpty()) {
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
