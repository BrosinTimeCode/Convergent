package View;

import com.googlecode.lanterna.TextColor;
import java.sql.Timestamp;
import java.time.Instant;

public class ConsoleLogItem {

    TextColor color;
    Timestamp timestamp;
    String memo;

    public ConsoleLogItem(TextColor color, String memo) {
        timestamp = Timestamp.from(Instant.now());
        this.color = color;
        this.memo = memo;
    }

    public String toString() {
        return timestamp.toString() + " " + memo;
    }
}
