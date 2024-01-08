package entities.users.functionalities;

import entities.helpers.Notification;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class NotificationsHandler {
    @Getter
    private List<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }
    public void deleteAllNotifications() {
        notifications = new ArrayList<>();
    }
}
