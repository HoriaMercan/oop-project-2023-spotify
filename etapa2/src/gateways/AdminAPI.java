package gateways;

import entities.audioFiles.AudioFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class AdminAPI {

    /**
     * @param audioFiles List of audio files
     * @return Either there are audio files with the same name in the list
     */
    public static boolean audioFilesRepeated(List<? extends AudioFile> audioFiles) {
        Set<String> nameSet = new HashSet<>(audioFiles.stream().map(AudioFile::getName).toList());

        return nameSet.size() != audioFiles.size();
    }

    public static void removeAlbum() {}
}
