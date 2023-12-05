package entities.users.functionalities;

import lombok.Getter;
import lombok.Setter;
import pagesystem.EnumPages;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


@Getter
@Setter
public final class PageHandler {
    private final Map<EnumPages, Supplier<String>> pages = new HashMap<>();
    private String contentCreatorPage = "";
    @Getter
    private EnumPages currentPage = EnumPages.HOME;

    /**
     * @param currentPage page to be set as current in the user's interface
     */
    public void setCurrentPage(final EnumPages currentPage) {
        this.currentPage = currentPage;
        System.out.println("Setted to " + currentPage);
    }

    /**
     * @return show the content of the current page
     */
    public String getCurrentPageContent() {
        System.out.println(pages.get(currentPage));
        return pages.get(currentPage).get();
    }

    /**
     * @param page page type
     * @param pageContent page content to be shown when page switched here
     */
    public void addPage(final EnumPages page, final Supplier<String> pageContent) {
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
}
