package entities.users.functionalities;

import entities.helpers.Notification;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * notifications handler class used by user
 */
public final class NotificationsHandler {
    @Getter
    private List<Notification> notifications = new ArrayList<>();

    /**
     * @param notification add notification to user's list
     */
    public void addNotification(final Notification notification) {
        notifications.add(notification);
    }

    /**
     * reset notifications list
     */
    public void deleteAllNotifications() {
        notifications = new ArrayList<>();
    }
}
