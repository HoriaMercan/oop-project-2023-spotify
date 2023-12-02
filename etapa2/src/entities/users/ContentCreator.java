package entities.users;

import entities.audioCollections.AudioCollection;
import entities.audioFiles.AudioFile;

import java.util.List;

public interface ContentCreator {
    public List<? extends AudioCollection> getContent();
}
