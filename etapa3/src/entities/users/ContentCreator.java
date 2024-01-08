package entities.users;

import entities.audioCollections.AudioCollection;
import entities.helpers.Notification;

import java.util.List;

/**
 * Content creator defines the basic functionality for any artist/host: those user types
 * stores audio collection
 */
public interface ContentCreator {
    /**
     * @return list of audio collection subtypes owned by the content creator
     */
    List<? extends AudioCollection> getContent();

    boolean addSubscriber(User user);
    void sendNotificationToSubscribers(Notification notification);
}
