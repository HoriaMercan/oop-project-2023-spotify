package pagesystem;

/**
 * an object is pageable if it can have a page associated
 */
public interface Pageable {
    /**
     * @return page content
     */
    String getPageContent();
}
