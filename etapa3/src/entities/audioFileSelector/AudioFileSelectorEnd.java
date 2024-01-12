package entities.audioFileSelector;

/**
 * Interface used to decide whether the player is at its end or not
 */
@FunctionalInterface
public interface AudioFileSelectorEnd {
    /**
     * @return decide whether the player is at its end or not
     */
    boolean end();
}
