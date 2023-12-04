package entities.users.functionalities;

import lombok.Getter;
import lombok.Setter;
import page_system.EnumPages;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


@Getter
@Setter
public final class PageHandler {
    private Map<EnumPages, Supplier<String>> pages = new HashMap<>();
    private String contentCreatorPage = "";

    public void setCurrentPage(EnumPages currentPage) {
        this.currentPage = currentPage;
        System.out.println("Setted to " + currentPage);
    }

    @Getter
    private EnumPages currentPage = EnumPages.HOME;

    public String getCurrentPageContent() {
        System.out.println(pages.get(currentPage));
        return pages.get(currentPage).get();
    }

    public void addPage(EnumPages page, Supplier<String> pageContent) {
        pages.put(page, pageContent);
    }

    public boolean removePage(EnumPages page) {
        boolean b = pages.containsKey(page);
//        currentPage = EnumPages.HOME;
        if (b) {
            pages.remove(page);
        }
        return b;
    }

    public boolean hasPage(EnumPages page) {
        return pages.containsKey(page);
    }

    public void removeNonStandardPages() {
        removePage(EnumPages.HOST);
        removePage(EnumPages.ARTIST);
    }
}
