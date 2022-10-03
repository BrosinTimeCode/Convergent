package com.brosintime.rts.Model;

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
}
