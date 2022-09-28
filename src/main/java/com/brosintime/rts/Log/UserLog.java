package com.brosintime.rts.Log;

import com.brosintime.rts.Log.PageBook.Page;
import java.util.ArrayList;
import java.util.List;

public interface UserLog {

    List<UserLogItem> LOGS = new ArrayList<>();

    static void add(int index, UserLogItem log) {
        LOGS.add(index, log);
    }

    static void add(UserLogItem log) {
        LOGS.add(log);
    }

    static void add(Page page) {
        LOGS.addAll(page.getAllLines());
    }
}
