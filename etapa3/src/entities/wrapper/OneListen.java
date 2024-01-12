package entities.wrapper;

import entities.users.User;
import lombok.Getter;

/**
 * This class represents one listen made by a user made for an audio file
 */
@Getter
public final class OneListen {
    private final User user;

    private final Listenable audioFile;

    private OneListen(final Builder builder) {
        user = builder.user;
        audioFile = builder.audioFile;
    }

    /**
     * Builder class for OneListen
     */
    public static final class Builder {
        private User user = null;
        private Listenable audioFile = null;

        /**
         * @param userBuild setUser
         * @return same object
         */
        public Builder setUser(final User userBuild) {
            Builder.this.user = userBuild;
            return Builder.this;
        }

        /**
         * @param audioFileBuild setAudioFile
         * @return same object
         */
        public Builder setAudioFile(final Listenable audioFileBuild) {
            Builder.this.audioFile = audioFileBuild;
            return Builder.this;
        }

        /**
         * @return OneListen object with parameters specified
         */
        public OneListen build() {
            return new OneListen(this);
        }

    }

    public static final Builder BUILDER = new Builder();

}
