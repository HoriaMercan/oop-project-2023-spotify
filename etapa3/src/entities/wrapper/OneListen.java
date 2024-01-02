package entities.wrapper;

import entities.audioFiles.AudioFile;
import entities.users.AbstractUser;
import entities.users.User;
import lombok.Getter;

/**
 * This class represents one listen made by a user made for an audio file
 */
@Getter
public class OneListen {
    private final User user;

    // Aici trebuie un Visitable
    private final Listenable audioFile;

    private OneListen(Builder builder) {
        user = builder.user;
        audioFile = builder.audioFile;
    }

    public static class Builder {
        private User user = null;
        private Listenable audioFile = null;

        public Builder setUser(User user) {
            Builder.this.user = user;
            return Builder.this;
        }

        public Builder setAudioFile(Listenable audioFile) {
            Builder.this.audioFile = audioFile;
            return Builder.this;
        }

        public OneListen build() {
            return new OneListen(this);
        }

    }

    public static Builder builder = new Builder();

}
