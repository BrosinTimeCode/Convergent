package com.brosintime.rts.Units;

/**
 * Can be selected by the player.
 */
public interface Selectable {

    /**
     * Adds this to selection.
     */
    void select();

    /**
     * Removes this from selection.
     */
    void deselect();

    /**
     * Determines if this is selected.
     *
     * @return true if selected, false if not
     */
    boolean isSelected();
}
