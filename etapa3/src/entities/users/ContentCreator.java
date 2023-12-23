package entities.users;

import entities.audioCollections.AudioCollection;

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
}
