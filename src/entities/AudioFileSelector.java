package entities;

import entities.audioFiles.AudioFile;

import java.util.List;

interface AudioFileSelector {

	boolean end();
	AudioFile current();
	Integer currentIndex();
	void next();
}
