package Log;

import Log.UserLogItem.Type;
import com.googlecode.lanterna.TextColor;
import java.util.ArrayList;
import java.util.List;

public interface PageBook {

    class Page {

        private final List<UserLogItem> log = new ArrayList<>();
        private final int lines;
        private final String header;

        private Page(int lines, String header) {
            this.lines = lines;
            this.header = header;
        }

        public List<UserLogItem> convert(List<UserLogItem> list) {
            this.log.add(new UserLogItem(TextColor.ANSI.YELLOW_BRIGHT, this.header, Type.INFO));
            for (int i = 0; i < lines; i++) {
                if (list.size() == 0 | this.log.size() >= lines) {
                    break;
                }
                this.log.add(list.get(0));
                list.remove(0);
            }
            return list;
        }

        public List<UserLogItem> getAllItems() {
            return this.log;
        }
    }

    static void paginateAndGetPage(String title, String commandAlias, int lines,
      List<UserLogItem> list, int pageNumber) {
        if (list.size() < lines - 1) {
            for (UserLogItem log : list) {
                UserLog.add(log);
            }
            return;
        }
        if (list.size() < 1) {
            UserLog.add(new UserLogItem(TextColor.ANSI.RED,
              "PageBook could not paginate because the list is empty!", Type.DEV));
            return;
        }
        final int TOTAL_PAGES = (int) Math.ceil((double) list.size() / (lines - 1));
        if (pageNumber < 1 | pageNumber > TOTAL_PAGES) {
            UserLog.add(
              new UserLogItem(TextColor.ANSI.RED, "Page " + pageNumber + " does not exist!",
                Type.INFO));
            return;
        }
        int i = 1;
        List<Page> pages = new ArrayList<>();
        while (list.size() > 0 && i <= pageNumber) {
            Page page = new Page(lines, title + " - Page (" + i + "/" + TOTAL_PAGES
              + ") - Type " + commandAlias + " (page)");
            list = new ArrayList<>(page.convert(list));
            pages.add(page);
            i++;
        }
        for (UserLogItem log : pages.get(pageNumber - 1).getAllItems()) {
            UserLog.add(log);
        }
    }
}
