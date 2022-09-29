package com.brosintime.rts.Log;

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
        this.timestamp = Timestamp.from(Instant.now());
        this.color = color;
        this.memo = memo;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.timestamp + " [" + this.type + "] " + this.memo;
    }

    public TextColor color() {
        return color;
    }

    public Timestamp timestamp() {
        return timestamp;
    }

    public String memo() {
        return memo;
    }

    public Type type() {
        return this.type;
    }
}
