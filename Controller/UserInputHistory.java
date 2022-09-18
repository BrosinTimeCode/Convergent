package Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * The UserInputHistory class is used to store and access a chronological history of Strings from
 * user input. Strings are maintained in the order they were added, with duplicates moving to the
 * most recent position. Each object instantiated contains its own archive and a Browser, which is a
 * cursor that moves forward (method: next) and backward (method: previous) through the archive.
 */
public class UserInputHistory {

    private final List<String> history = new ArrayList<>();
    private int browserIndex = 0;

    /**
     * Adds a String record to the list at index 0 and sets the Browser index to 0. If the record
     * already exists in the list, the record is instead moved to index 0.
     *
     * @param record A String that should be the most recent user input.
     */
    public void add(String record) {
        this.history.remove(record);
        this.history.add(0, record);
        this.browserIndex = 0;
    }

    /**
     * Gets the previous record from the list by the Browser chronologically and moves the Browser
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
     * Gets the next record from the list by the Browser chronologically and moves the Browser
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
}
