package com.brosintime.rts.Log;

import com.brosintime.rts.Log.PageBook.Page;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface stores a chronological index of {@link UserLogItem}.
 */
public interface UserLog {

    /**
     * A static list of {@link UserLogItem}.
     */
    List<UserLogItem> LOGS = new ArrayList<>();

    /**
     * Adds a {@link UserLogItem} at the provided index.
     * @param index in list
     * @param log to add
     */
    static void add(int index, UserLogItem log) {
        LOGS.add(index, log);
    }

    /**
     * Adds a {@link UserLogItem} as the last item in {@link #LOGS}.
     * @param log to add
     */
    static void add(UserLogItem log) {
        LOGS.add(log);
    }

    /**
     * Adds all lines of a {@link Page} to the end of the list.
     * @param page with lines of {@link UserLogItem}
     */
    static void add(Page page) {
        LOGS.addAll(page.getAllLines());
    }
}
