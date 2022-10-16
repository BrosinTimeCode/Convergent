package com.brosintime.rts.Model;

/**
 * A cursor owned and operated by a player.
 */
public interface PlayerCursor {

    /**
     * Retrieves the player who owns this cursor.
     *
     * @return the player
     */
    Player player();

}
