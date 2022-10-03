package com.brosintime.rts.Model;

import java.util.UUID;

public record Player(UUID id, String name, Team team) {

    public enum Team {
        BLUE,
        RED
    }
}
