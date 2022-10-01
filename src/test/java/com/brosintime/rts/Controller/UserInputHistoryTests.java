package com.brosintime.rts.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserInputHistoryTests {

    @Test
    void whenBrowsingLastItem_browsingNextRecord_returnsBlank() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("last");
        String prev1 = userInputHistory.previous();
        assertEquals("", userInputHistory.next());
    }

    @Test
    void whenBrowsingFirstItem_browsingPreviousRecord_returnsFirstItem() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("last");
        String prev1 = userInputHistory.previous();
        String prev2 = userInputHistory.previous();
        assertEquals("first", userInputHistory.previous());
    }

    @Test
    void whenBrowsingAnyButLastItem_browsingNextRecord_returnsNextRecord() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("second");
        userInputHistory.add("last");
        String prev1 = userInputHistory.previous();
        String prev2 = userInputHistory.previous();
        String prev3 = userInputHistory.previous();
        assertEquals("second", userInputHistory.next());
    }

    @Test
    void whenBrowsingAnyButFirstItem_browsingPreviousRecord_returnsPreviousRecord() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("second");
        userInputHistory.add("last");
        String prev1 = userInputHistory.previous();
        assertEquals("second", userInputHistory.previous());
    }

    @Test
    void whenAddingOneRecord_browsingPreviousRecord_returnsSameRecord() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("last");
        assertEquals("last", userInputHistory.previous());
    }

    @Test
    void whenAddingDuplicateRecord_browsingPreviousRecord_returnsSameRecord() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("second");
        userInputHistory.add("first");
        assertEquals("first", userInputHistory.previous());
    }

    @Test
    void addingDuplicateRecords_doesNotIncreaseHistorySize() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("second");
        userInputHistory.add("first");
        assertEquals(2, userInputHistory.size());
    }

    @Test
    void withEmptyHistory_browsingNextRecord_returnsBlank() {
        UserInputHistory userInputHistory = new UserInputHistory();
        assertEquals("", userInputHistory.next());
    }

    @Test
    void withEmptyHistory_browsingPreviousRecord_returnsBlank() {
        UserInputHistory userInputHistory = new UserInputHistory();
        assertEquals("", userInputHistory.previous());
    }

    @Test
    void emptyHistory_returnsSizeZero() {
        UserInputHistory userInputHistory = new UserInputHistory();
        assertEquals(0, userInputHistory.size());
    }

    @Test
    void addingAUniqueRecord_increasesSizeByOne() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        int sizeBeforeAddition = userInputHistory.size();
        userInputHistory.add("second");
        int sizeAfterAddition = userInputHistory.size();
        assertEquals(sizeBeforeAddition + 1, sizeAfterAddition);
    }

    @Test
    void withAnySizeHistory_clearingHistory_reducesSizeToZero() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("second");
        userInputHistory.clear();
        assertEquals(0, userInputHistory.size());
    }

    @Test
    void whenBrowsingAnyButLastItem_addingARecordThenBrowsingPrevious_returnsAddedRecord() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("second");
        userInputHistory.add("third");
        String prev1 = userInputHistory.previous();
        String prev2 = userInputHistory.previous();
        userInputHistory.add("fourth");
        assertEquals("fourth", userInputHistory.previous());
    }

    @Test
    void whenBrowsingAnyButLastItem_addingARecordThenBrowsingNext_returnsBlank() {
        UserInputHistory userInputHistory = new UserInputHistory();
        userInputHistory.add("first");
        userInputHistory.add("second");
        userInputHistory.add("third");
        String prev1 = userInputHistory.previous();
        String prev2 = userInputHistory.previous();
        userInputHistory.add("fourth");
        assertEquals("", userInputHistory.next());
    }
}
