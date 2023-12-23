package entities.audioFileSelector;

/**
 * Helper interface (like a piece of puzzle) used for selector
 * This interface handles getting the next element in a sequence,
 * without considering what happens after it ends (it's a primitive next)
 */
public interface AudioFileSelectorNext {
    /**
     * Next function will select the primitive following element
     * from a selector interface
     */
    void next();
}
