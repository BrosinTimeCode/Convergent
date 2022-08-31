package View;

import com.googlecode.lanterna.TextColor;
import java.sql.Timestamp;
import java.time.Instant;

public class ConsoleLogItem {

    private final TextColor color;
    private final Timestamp timestamp;
    private final String memo;

    public ConsoleLogItem(TextColor color, String memo) {
        timestamp = Timestamp.from(Instant.now());
        this.color = color;
        this.memo = memo;
    }

    public String toString() {
        return timestamp.toString() + " " + memo;
    }

    public TextColor getColor() {
        return color;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getMemo() {
        return memo;
    }
}
