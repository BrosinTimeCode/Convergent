package com.brosintime.rts.Model;

import com.brosintime.rts.View.Screen.Drawable.ColorCode;
import java.util.UUID;

/**
 * A player. Each player has a UUID, a human-readable name, and a team color.
 *
 * @param id   a UUID
 * @param name the player’s name
 * @param team the player’s team color
 */
public record Player(UUID id, String name, Team team) {

    public enum Team {
        BLUE,
        RED
    }

    @Override
    public String toString() {
        switch (this.team) {
            case RED -> {
                return ColorCode.RED.fgColor() + this.name + ColorCode.RESET.fgColor();
            }
            case BLUE -> {
                return ColorCode.BLUE.fgColor() + this.name + ColorCode.RESET.fgColor();
            }
            default -> {
                return this.name;
            }
        }
    }
}
