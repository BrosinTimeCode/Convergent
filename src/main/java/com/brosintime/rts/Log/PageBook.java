package com.brosintime.rts.Log;

import com.brosintime.rts.Log.UserLogItem.Type;
import com.googlecode.lanterna.TextColor;
import java.util.ArrayList;
import java.util.List;

/**
 * The PageBook class is both an object and a utility class. Instantiated as an object, a PageBook
 * is an ordered list of {@link Page}s. As a utility class, it provides static methods to paginate a
 * list of {@link UserLogItem}s and either generate a full PageBook, or a single Page as if it was
 * torn from an abstract PageBook.
 */
public class PageBook {

    /**
     * The Page class is a list of {@link UserLogItem}s and can be stored in a {@link PageBook}. In
     * addition to UserLogItems, a page may have a header, which is a single UserLogItem at index
     * 0.
     */
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

        /**
         * Retrieves the line count of this page.
         *
         * @return line count
         */
        public int size() {
            return list.size();
        }

        /**
         * Retrieves a specific line of this page by index.
         *
         * @param lineNumber index
         * @return {@link UserLogItem} at index
         */
        public UserLogItem get(int lineNumber) {
            return this.list.get(lineNumber);
        }

        /**
         * Retrieves all lines of this page as a {@link List} of {@link UserLogItem}s.
         *
         * @return list of logs
         */
        public List<UserLogItem> getAllLines() {
            return list;
        }

        /**
         * Determines if this page has a header.
         *
         * @return true if header exists, false if not
         */
        public boolean hasHeader() {
            return header != null;
        }

        /**
         * Determines if a specific line occurs in this page.
         *
         * @param item line to find
         * @return true if line occurs, false if not
         */
        public boolean contains(UserLogItem item) {
            return this.list.contains(item);
        }
    }

    private final List<Page> pages;

    private PageBook(List<Page> pages) {
        this.pages = new ArrayList<>(pages);
    }

    /**
     * Generates a {@link PageBook} object from a list of {@link UserLogItem}s. If the book has more
     * than one page, a title is added as a header to each page that reads:
     * <p><i>title</i> - Page (<i>current</i>/<i>total</i>) - Type “<i>commandName</i> [page]”
     *
     * @param title        the title at the top of each page
     * @param commandName  the alias of the command used to guide the player to specific pages
     * @param linesPerPage to split each page
     * @param list         of {@link UserLogItem}s
     * @return a {@link PageBook} object containing a paginated list of {@link UserLogItem}s
     */
    public static PageBook fromUserLogList(String title, String commandName, int linesPerPage,
        List<UserLogItem> list) {

        if (list.size() <= linesPerPage) {
            List<Page> book = new ArrayList<>();
            book.add(new Page(null, list));
            return PageBook.fromPages(book);
        }

        linesPerPage--;

        final int TOTAL_PAGES = (int) Math.ceil((double) list.size() / (linesPerPage));

        List<Page> book = new ArrayList<>();
        for (int i = 1; i < TOTAL_PAGES + 1; i++) {
            String header =
                title + " - Page (" + (i + 1) + "/" + TOTAL_PAGES + ") - Type \"" + commandName
                    + " [page]\"";

            book.add(new Page(
                header, list.subList((i - 1) * (linesPerPage),
                i == TOTAL_PAGES ? list.size() : i * (linesPerPage))));
        }
        return PageBook.fromPages(book);
    }

    private static PageBook fromPages(List<Page> book) {
        return new PageBook(book);
    }

    /**
     * Retrieves the page count of this book.
     *
     * @return number of pages
     */
    public int size() {
        return this.pages.size();
    }

    /**
     * Retrieves a {@link Page} object by the index provided.
     *
     * @param pageNumber to retrieve
     * @return {@link Page} object
     */
    public Page get(int pageNumber) {
        return pages.get(pageNumber);
    }

    /**
     * Static method that combines {@link #fromUserLogList} and {@link #get} to retrieve a specific
     * page without instantiating a {@link PageBook} object. The provided list is split by the
     * requested line count and if there should be more than one page, a header is added to the top
     * of the page that reads:
     * <p><i>title</i> - Page (<i>current</i>/<i>total</i>) - Type “<i>commandName</i> [page]”
     * <p>If requesting a higher page number than the actual page count, the last page is returned.
     * If requesting a negative page, the first page is returned.
     *
     * @param title        the title at the top of each page
     * @param commandName  the alias of the command used to guide the player to specific pages
     * @param linesPerPage to split each page
     * @param list         of {@link UserLogItem}s
     * @param pageNumber   to retrieve
     * @return a {@link Page} by requested page number
     */
    public static Page paginateAndGetPage(String title, String commandName, int linesPerPage,
        List<UserLogItem> list, int pageNumber) {

        if (list.size() <= linesPerPage) {
            return new Page(null, list);
        }

        linesPerPage--;

        final int TOTAL_PAGES = (int) Math.ceil((double) list.size() / (linesPerPage));

        if (pageNumber > TOTAL_PAGES) {
            pageNumber = TOTAL_PAGES;
        }
        final String header =
            title + " - Page (" + pageNumber + "/" + TOTAL_PAGES + ") - Type \"" + commandName
                + " [page]\"";

        List<UserLogItem> newList = list.subList((pageNumber - 1) * (linesPerPage),
            pageNumber == TOTAL_PAGES ? list.size() : pageNumber * (linesPerPage));
        return new Page(header, newList);
    }

    /**
     * Retrieves a list of all pages in this book.
     *
     * @return all {@link Page}s
     */
    public List<Page> getAll() {
        return this.pages;
    }

    /**
     * Determines if a specific line occurs anywhere inside this book.
     *
     * @param item to look for
     * @return true if line occurs, false if not
     */
    public boolean contains(UserLogItem item) {
        for (Page page : this.pages) {
            if (page.contains(item)) {
                return true;
            }
        }
        return false;
    }
}