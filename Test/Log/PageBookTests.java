package Test.Log;

import Log.PageBook;
import Log.PageBook.Page;
import Log.UserLogItem;
import Log.UserLogItem.Type;
import com.googlecode.lanterna.TextColor;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class PageBookTests {

    @Test
    void paginatingUserLogList_withLessItemsThanItemsPerPage_returnsOnePage() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 5;
        int linesPerPage = 6;
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook = PageBook.fromUserLogList("", "", linesPerPage, log);
        assertEquals(1, pageBook.size());
    }

    // Paginating a list resulting in a single page should return an identical list and should not
    // have additional lines like the page title
    @Test
    void pageBook_withOnePage_hasSameLineCountAsInputList() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 10;
        int linesPerPage = 10;
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook = PageBook.fromUserLogList("", "", linesPerPage, log);
        assertEquals(1, pageBook.size());
        assertEquals(itemCount, pageBook.get(0).size());
    }

    @Test
    void pageBook_withLessPagesThanRequested_returnsLastPage() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 10;
        int linesPerPage = 8; // total of 2 pages
        int pageNumber = 5; // ask for page 5 but should get last page (2) instead
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        Page page5 = PageBook.paginateAndGetPage("", "", linesPerPage, log, pageNumber);
        Page page2 = PageBook.paginateAndGetPage("", "", linesPerPage, log, pageNumber);
        assertEquals(page2, page5);
    }

    @Test
    void paginatingUserLogList_withSameItemsAsItemsPerPage_returnsTwoPages() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 5;
        int linesPerPage = 5;
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook = PageBook.fromUserLogList("", "", linesPerPage, log);
        assertEquals(2, pageBook.size());
    }

    @Test
    void pageBookWithPreviousPaginationCall_callingPagination_returnsOnlyNewPagination() {
        List<UserLogItem> log1 = new ArrayList<>();
        for (int i=0; i<3; i++) {
            log1.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        List<UserLogItem> log2 = new ArrayList<>();
        for (int i=0; i<2; i++) {
            log2.add(new UserLogItem(TextColor.ANSI.WHITE, "line" + i, Type.INFO));
        }
        PageBook pageBook1 = PageBook.fromUserLogList("", "", 10, log1);
        Page pageA = pageBook1.get(0);
        PageBook pageBook2 = PageBook.fromUserLogList("", "", 10, log2);
        Page pageB = pageBook2.get(0);
        PageBook pageBook3 = PageBook.fromUserLogList("", "", 10, log1);
        Page pageC = pageBook3.get(0);
        assertNotEquals(pageA, pageB);
        assertEquals(pageA, pageC);
    }

    @Test
    void paginatingAnEmptyList_returnsAnEmptyPage() {
        List<UserLogItem> log = new ArrayList<>();
        PageBook pageBook = PageBook.fromUserLogList("", "", 10, log);
        assertEquals(log.size(), pageBook.get(0).size());
    }
}
