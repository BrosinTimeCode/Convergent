package com.brosintime.rts.Model.Log;

import com.brosintime.rts.Model.Player;
import com.brosintime.rts.View.Screen.Drawable.ColorCode;

public class ChatLogEntry {

    private final Player player;
    private final String memo;

    protected ChatLogEntry(Player player, String memo) {
        this.player = player;
        this.memo = ColorCode.RESET.fgColor() + ColorCode.RESET.bgColor() + memo;
    }

    protected ChatLogEntry(String memo) {
        this.player = null;
        this.memo = ColorCode.RESET.fgColor() + ColorCode.RESET.bgColor() + memo;
    }

    @Override
    public String toString() {
        if (this.player == null) {
            return this.memo;
        } else {
            return ColorCode.RESET.fgColor() + ColorCode.RESET.bgColor() + "<" + this.player + "> " + this.memo;
        }
    }

}
