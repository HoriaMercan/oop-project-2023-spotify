package entities.wrapper;


public interface Listenable {
    /**
     * @param visitor Visitor pattern implementation for accepting
     */
    void acceptListen(VisitorWrapper visitor);
}
