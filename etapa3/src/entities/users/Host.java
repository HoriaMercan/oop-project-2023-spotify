package entities.users;

import entities.audioCollections.AudioCollection;
import entities.audioCollections.Podcast;
import entities.helpers.Announcement;
import entities.helpers.Notification;
import entities.wrapper.statistics.HostWrapperStatistics;
import lombok.Getter;
import lombok.Setter;
import pagesystem.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class Host extends AbstractUser implements Pageable, ContentCreator {
    private final List<Podcast> podcasts = new ArrayList<>();
    private final List<Announcement> announcements = new ArrayList<>();

    public Host(final String username, final String city, final Integer age) {
        super(username, city, age);
        this.userType = UserType.HOST;
        wrapperStatistics = new HostWrapperStatistics();
    }

    /**
     * @param podcastName
     * @return verify if a host has a podcast \w given name
     */
    public boolean hasPodcast(final String podcastName) {
        List<Podcast> ans = podcasts.stream()
                .filter(p -> p.getName().equals(podcastName)).toList();

        return !ans.isEmpty();
    }

    /**
     * @param announcementName
     * @return verify if host has announcement \w given name
     */
    public boolean hasAnnouncement(final String announcementName) {
        List<Announcement> ans = announcements.stream()
                .filter(a -> a.getName().equals(announcementName)).toList();

        return !ans.isEmpty();
    }


    /**
     * @param name name of announcement to be deleted
     */
    public void removeAnnouncement(final String name) {
        List<Announcement> ans = announcements.stream()
                .filter(a -> a.getName().equals(name)).toList();

        if (!ans.isEmpty()) {
            announcements.remove(ans.get(0));
        }
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
        sb.append("]\n\nAnnouncements:\n\t[");

        if (!announcements.isEmpty()) {
            for (Announcement a : announcements) {
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

    private final List<User> subscribers = new ArrayList<>();
    @Override
    public boolean addSubscriber(final User user) {
        if (subscribers.contains(user)) {
            subscribers.remove(user);
            return false;
        }
        subscribers.add(user);
        return true;
    }

    @Override
    public void sendNotificationToSubscribers(final Notification notification) {
        for (User user: subscribers) {
            user.getNotificationsHandler().addNotification(notification);
        }
    }
}
