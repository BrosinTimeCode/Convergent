package View;

import com.googlecode.lanterna.TextColor;
import java.sql.Timestamp;
import java.time.Instant;

public class UserLogItem {

    private final TextColor color;
    private final Timestamp timestamp;
    private final String memo;
    private final Type type;

    public enum Type {
        DEV,
        CHAT,
        INFO
    }

    public UserLogItem(TextColor color, String memo, Type type) {
        timestamp = Timestamp.from(Instant.now());
        this.color = color;
        this.memo = memo;
        this.type = type;
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

    public Type getType() {
        return this.type;
    }
}
