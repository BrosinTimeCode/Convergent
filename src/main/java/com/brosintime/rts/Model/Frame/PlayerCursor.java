package com.brosintime.rts.Model.Frame;

import com.brosintime.rts.Model.Player;

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
