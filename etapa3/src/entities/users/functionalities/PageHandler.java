package entities.users.functionalities;

import lombok.Getter;
import lombok.Setter;
import pagesystem.EnumPages;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Supplier;


/**
 * Page handler object for navigation
 */
@Getter
@Setter
public final class PageHandler {
    private final Map<EnumPages, Supplier<String>> pages = new HashMap<>();

    private LinkedList<EnumPages> backPages = new LinkedList<>();
    private LinkedList<EnumPages> nextPages = new LinkedList<>();

    private String contentCreatorPage = "";
    @Getter
    private EnumPages currentPage = EnumPages.HOME;

    /**
     * @param currentPage page to be set as current in the user's interface
     */
    public void setCurrentPage(final EnumPages currentPage) {
        this.backPages.addLast(this.currentPage);
        this.currentPage = currentPage;
    }

    /**
     * Reset next pages array
     */
    public void resetNextPage() {
        this.nextPages = new LinkedList<>();
    }

    /**
     * @return show the content of the current page
     */
    public String getCurrentPageContent() {
//        System.out.println(pages.get(currentPage));
        return pages.get(currentPage).get();
    }

    /**
     * @param page page type
     * @param pageContent page content to be shown when page switched here
     */
    public void addPage(final EnumPages page, final Supplier<String> pageContent) {
        this.backPages.addLast(currentPage);
        pages.put(page, pageContent);
    }

    /**
     * @param page page that will not be used anymore
     * @return true if the page was in the list before this function call
     */
    public boolean removePage(final EnumPages page) {
        boolean b = pages.containsKey(page);
        if (b) {
            pages.remove(page);
        }
        return b;
    }

    /**
     * @param page
     * @return if this handler has a page or not
     */
    public boolean hasPage(final EnumPages page) {
        return pages.containsKey(page);
    }

    /**
     * make the page handler back to basic functionality
     */
    public void removeNonStandardPages() {
        removePage(EnumPages.HOST);
        removePage(EnumPages.ARTIST);
    }

    /**
     * Returns true whether you can go to the back with page and execute this command
     * */
    public boolean doBack() {
        if (this.backPages.isEmpty()) {
            return false;
        }

        this.currentPage = this.backPages.getLast();
        nextPages.addLast(backPages.removeLast());

        return true;
    }

    /**
     * Returns true whether you can go to the next with page and execute this command
     * */
    public boolean doNext() {
        if (nextPages.isEmpty()) {
            return false;
        }

        this.currentPage = this.nextPages.getLast();
        backPages.addLast(nextPages.removeLast());

        return true;
    }
}
