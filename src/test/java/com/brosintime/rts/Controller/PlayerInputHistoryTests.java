package com.brosintime.rts.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PlayerInputHistoryTests {

    @Test
    void whenBrowsingLastItem_browsingNextRecord_returnsBlank() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("last");
        String prev1 = playerInputHistory.previous();
        assertEquals("", playerInputHistory.next());
    }

    @Test
    void whenBrowsingFirstItem_browsingPreviousRecord_returnsFirstItem() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("last");
        String prev1 = playerInputHistory.previous();
        String prev2 = playerInputHistory.previous();
        assertEquals("first", playerInputHistory.previous());
    }

    @Test
    void whenBrowsingAnyButLastItem_browsingNextRecord_returnsNextRecord() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("second");
        playerInputHistory.add("last");
        String prev1 = playerInputHistory.previous();
        String prev2 = playerInputHistory.previous();
        String prev3 = playerInputHistory.previous();
        assertEquals("second", playerInputHistory.next());
    }

    @Test
    void whenBrowsingAnyButFirstItem_browsingPreviousRecord_returnsPreviousRecord() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("second");
        playerInputHistory.add("last");
        String prev1 = playerInputHistory.previous();
        assertEquals("second", playerInputHistory.previous());
    }

    @Test
    void whenAddingOneRecord_browsingPreviousRecord_returnsSameRecord() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("last");
        assertEquals("last", playerInputHistory.previous());
    }

    @Test
    void whenAddingDuplicateRecord_browsingPreviousRecord_returnsSameRecord() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("second");
        playerInputHistory.add("first");
        assertEquals("first", playerInputHistory.previous());
    }

    @Test
    void addingDuplicateRecords_doesNotIncreaseHistorySize() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("second");
        playerInputHistory.add("first");
        assertEquals(2, playerInputHistory.size());
    }

    @Test
    void withEmptyHistory_browsingNextRecord_returnsBlank() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        assertEquals("", playerInputHistory.next());
    }

    @Test
    void withEmptyHistory_browsingPreviousRecord_returnsBlank() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        assertEquals("", playerInputHistory.previous());
    }

    @Test
    void emptyHistory_returnsSizeZero() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        assertEquals(0, playerInputHistory.size());
    }

    @Test
    void addingAUniqueRecord_increasesSizeByOne() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        int sizeBeforeAddition = playerInputHistory.size();
        playerInputHistory.add("second");
        int sizeAfterAddition = playerInputHistory.size();
        assertEquals(sizeBeforeAddition + 1, sizeAfterAddition);
    }

    @Test
    void withAnySizeHistory_clearingHistory_reducesSizeToZero() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("second");
        playerInputHistory.clear();
        assertEquals(0, playerInputHistory.size());
    }

    @Test
    void whenBrowsingAnyButLastItem_addingARecordThenBrowsingPrevious_returnsAddedRecord() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("second");
        playerInputHistory.add("third");
        String prev1 = playerInputHistory.previous();
        String prev2 = playerInputHistory.previous();
        playerInputHistory.add("fourth");
        assertEquals("fourth", playerInputHistory.previous());
    }

    @Test
    void whenBrowsingAnyButLastItem_addingARecordThenBrowsingNext_returnsBlank() {
        PlayerInputHistory playerInputHistory = new PlayerInputHistory();
        playerInputHistory.add("first");
        playerInputHistory.add("second");
        playerInputHistory.add("third");
        String prev1 = playerInputHistory.previous();
        String prev2 = playerInputHistory.previous();
        playerInputHistory.add("fourth");
        assertEquals("", playerInputHistory.next());
    }
}
