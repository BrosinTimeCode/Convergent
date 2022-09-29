package com.brosintime.rts.Log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.brosintime.rts.Log.PageBook.Page;
import com.brosintime.rts.Log.UserLogItem.Type;
import com.googlecode.lanterna.TextColor;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PageBookTests {

    @Test
    void paginatingUserLogList_withSameOrLessItemsThanItemsPerPage_returnsOnePage() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 5;
        int linesPerPage = 6;
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook1 = PageBook.fromUserLogList("Test", "command", linesPerPage, log);
        Page page1 = PageBook.paginateAndGetPage("Test", "command", linesPerPage, log, 2);
        assertEquals(1, pageBook1.size());
        for (int i = 0; i < page1.size(); i++) {
            assertEquals(log.get(i), page1.get(i));
        }

        itemCount = 8;
        linesPerPage = 8;
        log.clear();
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook2 = PageBook.fromUserLogList("Test", "command", linesPerPage, log);
        Page page2 = PageBook.paginateAndGetPage("Test", "command", linesPerPage, log, 2);
        assertEquals(1, pageBook2.size());
        for (int i = 0; i < page2.size(); i++) {
            assertEquals(log.get(i), page2.get(i));
        }
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
        PageBook pageBook = PageBook.fromUserLogList("Test", "command", linesPerPage, log);
        assertEquals(1, pageBook.size());
        assertEquals(itemCount, pageBook.get(0).size());
    }

    @Test
    void pageBook_requestingPageOneOfMultiple_returnsPageWithHeader() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 10;
        int linesPerPage = 5;
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook = PageBook.fromUserLogList("Test", "command", linesPerPage, log);
        Page page = PageBook.paginateAndGetPage("Test", "command", linesPerPage, log, 1);
        assertTrue(pageBook.get(0).hasHeader());
        assertTrue(page.hasHeader());
    }

    @Test
    void pageBook_createdWithMultiplePages_hasAllOriginalLines() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 16;
        int linesPerPage = 5;
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook = PageBook.fromUserLogList("Test", "command", linesPerPage, log);
        for (UserLogItem i : log) {
            assertTrue(pageBook.contains(i));
        }
    }

    @Test
    void pageBookWithMultiplePages_pageLinesEqualLinesPerPage() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 10;
        int linesPerPage = 5;
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook = PageBook.fromUserLogList("Test", "command", linesPerPage, log);
        Page page = PageBook.paginateAndGetPage("Test", "command", linesPerPage, log, 1);
        for (Page p : pageBook.getAll()) {
            assertTrue(p.size() <= linesPerPage);
        }
        assertEquals(linesPerPage, page.size());
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
        Page page5 = PageBook.paginateAndGetPage("Test", "command", linesPerPage, log, pageNumber);
        Page page2 = PageBook.paginateAndGetPage("Test", "command", linesPerPage, log, 2);
        for (int i = 1; i < page5.size(); i++) {
            assertEquals(page2.get(i), page5.get(i));
        }
    }

    @Test
    void paginatingUserLogList_withSameItemsAsItemsPerPage_returnsOnePage() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 5;
        int linesPerPage = 5;
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook = PageBook.fromUserLogList("Test", "command", linesPerPage, log);
        assertEquals(1, pageBook.size());
    }

    @Test
    void paginatingUserLogList_withTwiceItemsAsPerPage_returnsThreePages() {
        List<UserLogItem> log = new ArrayList<>();
        int itemCount = 10;
        int linesPerPage = 5;
        for (int i = 0; i < itemCount; i++) {
            log.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        PageBook pageBook = PageBook.fromUserLogList("Test", "command", linesPerPage, log);
        assertEquals(3, pageBook.size());
    }

    @Test
    void pageBookWithPreviousPaginationCall_callingPagination_returnsOnlyNewPagination() {
        List<UserLogItem> log1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            log1.add(new UserLogItem(TextColor.ANSI.WHITE, "item" + i, Type.INFO));
        }
        List<UserLogItem> log2 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            log2.add(new UserLogItem(TextColor.ANSI.WHITE, "line" + i, Type.INFO));
        }
        PageBook pageBook1 = PageBook.fromUserLogList("Test", "command", 10, log1);
        Page pageA = pageBook1.get(0);
        PageBook pageBook2 = PageBook.fromUserLogList("Test", "command", 10, log2);
        Page pageB = pageBook2.get(0);
        PageBook pageBook3 = PageBook.fromUserLogList("Test", "command", 10, log1);
        Page pageC = pageBook3.get(0);
        assertFalse(pageA.getAllLines().containsAll(pageB.getAllLines()) && pageB.getAllLines()
            .containsAll(pageA.getAllLines()));
        assertTrue(pageA.getAllLines().containsAll(pageC.getAllLines()) && pageC.getAllLines()
            .containsAll(pageA.getAllLines()));
    }

    @Test
    void paginatingAnEmptyList_returnsAnEmptyPage() {
        List<UserLogItem> log = new ArrayList<>();
        PageBook pageBook = PageBook.fromUserLogList("Test", "command", 10, log);
        assertEquals(log.size(), pageBook.get(0).size());
    }
}
