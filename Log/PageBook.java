package Log;

import Log.UserLogItem.Type;
import com.googlecode.lanterna.TextColor;
import java.util.ArrayList;
import java.util.List;

public class PageBook {

    public static class Page {

        private final List<UserLogItem> list = new ArrayList<>();
        private final String header;

        private Page(String header, List<UserLogItem> list) {
            this.header = header;
            if (this.header != null) {
                this.list.add(
                  new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT, this.header, Type.INFO));
            }
            this.list.addAll(list);
        }

        public int size() {
            return list.size();
        }

        public UserLogItem get(int lineNumber) {
            return this.list.get(lineNumber);
        }

        public List<UserLogItem> getAllLines() {
            return list;
        }

        public boolean hasHeader() {
            return header != null;
        }

        public boolean contains(UserLogItem item) {
            return this.list.contains(item);
        }
    }

    private final List<Page> pages;

    private PageBook(List<Page> pages) {
        this.pages = new ArrayList<>(pages);
    }

    public static PageBook fromUserLogList(String title, String commandName, int linesPerPage,
      List<UserLogItem> list) {

        // IF the list fits on one page, return a Page without a header
        if (list.size() <= linesPerPage) {
            List<Page> book = new ArrayList<>();
            book.add(new Page(null, list));
            return PageBook.fromPages(book);
        }

        // ELSE linesPerPage is decremented by 1 to make room for a header
        linesPerPage--;

        // Rounds up the quotient of (total lines) / (lines per page)
        // linesPerPage is decremented to make room for the header
        final int TOTAL_PAGES = (int) Math.ceil((double) list.size() / (linesPerPage));

        List<Page> book = new ArrayList<>();
        for (int i = 1; i < TOTAL_PAGES + 1; i++) {
            // Sets header to:
            // <title> - Page (<current>/<total>) - Type "command <page>"
            String header =
              title + " - Page (" + (i + 1) + "/" + TOTAL_PAGES + ") - Type \"" + commandName
                + " [page]\"";

            // Create a sublist for each page; flexes to account for potentially fewer lines on
            // the last page
            book.add(new Page(
              header, list.subList((i - 1) * (linesPerPage),
              i == TOTAL_PAGES ? list.size() : i * (linesPerPage))));
        }
        return PageBook.fromPages(book);
    }

    private static PageBook fromPages(List<Page> book) {
        return new PageBook(book);
    }

    public int size() {
        return this.pages.size();
    }

    public Page get(int pageNumber) {
        return pages.get(pageNumber);
    }

    public static Page paginateAndGetPage(String title, String commandName, int linesPerPage,
      List<UserLogItem> list, int pageNumber) {

        // IF the list fits on one page, return a Page without a header
        if (list.size() <= linesPerPage) {
            return new Page(null, list);
        }

        // ELSE linesPerPage is decremented by 1 to make room for a header
        linesPerPage--;

        // Rounds up the quotient of (total lines) / (lines per page)
        // linesPerPage is decremented to make room for the header
        final int TOTAL_PAGES = (int) Math.ceil((double) list.size() / (linesPerPage));

        // If the user requested a page greater than actual page count, set to last page
        if (pageNumber > TOTAL_PAGES) {
            pageNumber = TOTAL_PAGES;
        }
        // Sets header to:
        // <title> - Page (<current>/<total>) - Type "command [page]"
        final String header =
          title + " - Page (" + pageNumber + "/" + TOTAL_PAGES + ") - Type \"" + commandName
            + " [page]\"";

        // Creates a sublist of <list> based on what lines would appear on <pageNumber>; flexes to
        // account for potentially fewer lines on the last page
        List<UserLogItem> newList = list.subList((pageNumber - 1) * (linesPerPage),
          pageNumber == TOTAL_PAGES ? list.size() : pageNumber * (linesPerPage));
        return new Page(header, newList);
    }

    public List<Page> getAll() {
        return this.pages;
    }

    public boolean contains(UserLogItem item) {
        for (Page page : this.pages) {
            if (page.contains(item)) {
                return true;
            }
        }
        return false;
    }
}