package entities.users;

import entities.audioCollections.Album;
import entities.audioCollections.AudioCollection;
import entities.audioCollections.Podcast;
import entities.helpers.Announcement;
import lombok.Getter;
import lombok.Setter;
import page_system.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Host extends AbstractUser implements Pageable, ContentCreator {
    private final List<Podcast> podcasts = new ArrayList<>();
    private final List<Announcement>announcements = new ArrayList<>();

    public Host(String username, String city, Integer age) {
        super(username, city, age);
        this.userType = UserType.HOST;
    }

    public boolean hasPodcast(String podcastName) {
        List<Podcast> ans = podcasts.stream()
                .filter(p -> p.getName().equals(podcastName)).toList();

        return !ans.isEmpty();
    }
    @Override
    public String getPageContent() {
        StringBuilder sb = new StringBuilder("Podcasts:\n\t[");

        if (!podcasts.isEmpty()) {
            for (Podcast p : podcasts) {
                sb.append(p).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]\n\nAnnouncements\n\t[");

        if (!announcements.isEmpty()) {
            for (Announcement a: announcements) {
                sb.append(a).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public List<? extends AudioCollection> getContent() {
        return this.podcasts;
    }
}
