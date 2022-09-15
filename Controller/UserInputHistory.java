package Controller;

import java.util.ArrayList;
import java.util.List;

public class UserInputHistory {

    private final List<String> history = new ArrayList<>();
    private int index = 0;

    public void add(String record) {
        this.history.remove(record);
        this.history.add(0, record);
        this.index = 0;
    }

    public String previous() {
        if (this.history.size() == 0) {
            return "";
        }
        if (this.index < this.history.size()) {
            this.index++;
        }
        return this.history.get(this.index - 1);
    }

    public String next() {
        if (this.index > 0) {
            this.index--;
        }
        if (this.index == 0) {
            return "";
        }
        return this.history.get(this.index - 1);
    }

    public int size() {
        return this.history.size();
    }

    public void clear() {
        this.history.clear();
    }
}
