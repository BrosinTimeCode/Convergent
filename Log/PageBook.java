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
    }

    private final List<Page> pages;

    private PageBook(List<Page> pages) {
        this.pages = new ArrayList<>(pages);
    }

    public static PageBook fromUserLogList(String title, String commandName, int linesPerPage,
      List<UserLogItem> list) {
        int pageCount = list.size() / (linesPerPage - 1);
        if (pageCount > 1) {
            pageCount++;
        }
        if (pageCount <= 1) {
            Page page = new Page(null, list);
            List<Page> book = new ArrayList<>();
            book.add(page);
            return PageBook.fromPages(book);
        }
        List<Page> book = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            if (i + 1 == pageCount) {
                book.add(new Page(
                  title + " - Page (" + (i + 1) + "/" + pageCount + ") - Type \"" + commandName
                    + " [page]\"", list.subList(i * linesPerPage, list.size())));
            } else {
                book.add(new Page(
                  title + " - Page (" + (i + 1) + "/" + pageCount + ") - Type \"" + commandName
                    + " [page]\"", list.subList(i * linesPerPage, (i + 1) * linesPerPage - 1)));
            }
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

        int pageCount = list.size() <= linesPerPage ? 1
          : (int) Math.ceil((double) list.size() / (linesPerPage - 1));
        if (pageNumber > pageCount) {
            pageNumber = pageCount;
        }
        if (pageCount <= 1) {
            return new Page(null, list);
        }
        String header =
          title + " - Page (" + pageNumber + "/" + pageCount + ") - Type \"" + commandName
            + " [page]\"";
        List<UserLogItem> newList;
        if (pageNumber == pageCount) {
            newList = list.subList((pageNumber - 1) * (linesPerPage - 1), list.size());
        } else {
            newList = list.subList((pageNumber - 1) * (linesPerPage - 1),
              pageNumber * (linesPerPage - 1));
        }
        return new Page(header, newList);
    }

    public List<Page> getAll() {
        return this.pages;
    }
}