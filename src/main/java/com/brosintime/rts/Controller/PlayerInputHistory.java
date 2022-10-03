package com.brosintime.rts.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * The PlayerInputHistory class is used to store and access a chronological history of Strings from
 * player input. Strings are maintained in the order they were added, with duplicates moving to the
 * most recent position. Each object instantiated contains its own archive and a Browser, which is a
 * cursor that moves forward ({@link #next()}) and backward ({@link #previous()}) through the
 * archive.
 */
public class PlayerInputHistory {

    private final List<String> history = new ArrayList<>();
    private int browserIndex = 0;

    /**
     * Adds a String record to the list at index 0 and sets the Browser index to 0. If the record
     * already exists in the list, the record is instead moved to index 0.
     *
     * @param record A String that should be the most recent player input.
     */
    public void add(String record) {
        this.history.remove(record);
        this.history.add(0, record);
        this.browserIndex = 0;
    }

    /**
     * Gets the previous record from the list by the Browser chronologically and moves the browser
     * backward by one. If the browser is at the first record, the first record is returned and the
     * Browser does not move. If the list is empty, an empty String is returned.
     *
     * @return The previous String record from the list chronologically, or empty if nonexistent.
     */
    public String previous() {
        if (this.history.size() == 0) {
            return "";
        }
        if (this.browserIndex < this.history.size()) {
            this.browserIndex++;
        }
        return this.history.get(this.browserIndex - 1);
    }

    /**
     * Gets the next record from the list by the Browser chronologically and moves the browser
     * forward by one. If the browser is at the last record or the list is empty, an empty String is
     * returned and the Browser does not move.
     *
     * @return The next String record from the list chronologically, or empty if nonexistent.
     */
    public String next() {
        if (this.browserIndex > 0) {
            this.browserIndex--;
        }
        if (this.browserIndex == 0) {
            return "";
        }
        return this.history.get(this.browserIndex - 1);
    }

    public int size() {
        return this.history.size();
    }

    public void clear() {
        this.history.clear();
    }

    /**
     * Converts a list of characters to a string.
     *
     * @param list the list of characters to convert
     * @return representation as a string
     */
    public static String charListToString(List<Character> list) {
        if (list.size() == 0) {
            return "";
        }
        return list.toString().substring(1, 3 * list.size() - 1).replaceAll(", ", "");
    }

    /**
     * Converts a string to a list of characters.
     *
     * @param string the string to convert
     * @return representation as a list of characters
     */
    public static List<Character> stringToCharList(String string) {
        List<Character> list = new ArrayList<>();
        for (char c : string.toCharArray()) {
            list.add(c);
        }
        return list;
    }


}
