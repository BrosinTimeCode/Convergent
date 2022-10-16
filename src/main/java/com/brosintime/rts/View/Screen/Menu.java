package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Model.Node;
import com.brosintime.rts.View.GameView;
import com.googlecode.lanterna.input.KeyStroke;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

/**
 * A menu is a list of options that can be cycled through by {@link #next()} and
 * {@link #previous()}. The currently selected option is highlighted and can be retrieved as a
 * string ID by {@link #getSelectedOption()}.
 * <p>A menu is readily outfitted to a certain {@link MenuStyle} or instantiated and manually
 * customized by setter methods.
 */
public class Menu extends Screen {

    private final Map<Integer, String> options = new HashMap<>();
    private int selectedIndex = 0;
    private MenuOrientation orientation = MenuOrientation.HORIZONTAL;
    private ColorCode defaultForegroundColor = ColorCode.WHITE;
    private ColorCode defaultBackgroundColor = ColorCode.BLACK;
    private ColorCode selectedForegroundColor = ColorCode.GREEN;
    private ColorCode selectedBackgroundColor = ColorCode.BLACK;
    private char selectedSymbol = ' ';
    private int padding = 0;

    @Override
    public void onFocus() {

    }

    @Override
    public void offFocus() {

    }

    @Override
    public void onKeyPress(KeyStroke key) {

    }

    /**
     * The orientation of the menu.
     */
    public enum MenuOrientation {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * A template to style the menu.
     */
    public enum MenuStyle {
        DEFAULT(
            MenuOrientation.HORIZONTAL,
            ColorCode.WHITE,
            ColorCode.BLACK,
            ColorCode.GREEN,
            ColorCode.BLACK,
            ' ',
            0
        ),
        TITLE_SCREEN(
            MenuOrientation.VERTICAL,
            ColorCode.WHITE,
            ColorCode.BLACK,
            ColorCode.GREEN,
            ColorCode.BLACK,
            '>',
            1
        );
        private final MenuOrientation orientation;
        private final ColorCode defaultForegroundColor;
        private final ColorCode defaultBackgroundColor;
        private final ColorCode selectedForegroundColor;
        private final ColorCode selectedBackgroundColor;
        private final char selectedSymbol;
        private final int padding;

        MenuStyle(
            MenuOrientation orientation,
            ColorCode defaultForegroundColor,
            ColorCode defaultBackgroundColor,
            ColorCode selectedForegroundColor,
            ColorCode selectedBackgroundColor,
            char selectedSymbol,
            int padding) {
            this.orientation = orientation;
            this.defaultForegroundColor = defaultForegroundColor;
            this.defaultBackgroundColor = defaultBackgroundColor;
            this.selectedForegroundColor = selectedForegroundColor;
            this.selectedBackgroundColor = selectedBackgroundColor;
            this.selectedSymbol = selectedSymbol;
            this.padding = padding;
        }
    }

    /**
     * Constructs a new Menu with the provided style. If a custom style is desired,
     * {@link MenuStyle#DEFAULT} should be passed and then setter methods should be used to style
     * the menu. An {@link IllegalArgumentException} is thrown if the provided game client is null,
     * and this origin is set to (0, 0) if the provided origin is null.
     *
     * @param client the game client to interface with
     * @param origin the Node to set where to start drawing the menu
     * @param style  the starting style of the menu
     */
    public Menu(GameView client, Node origin, MenuStyle style) {

        if (client == null) {
            throw new IllegalArgumentException("Menu created with null game client");
        }
        this.client = client;
        this.origin = origin != null ? origin : new Node(0, 0);
        this.setStyle(style);

    }

    /**
     * Sets the theme of the menu to the provided style.
     *
     * @param style the new style to set
     */
    public void setStyle(MenuStyle style) {
        if (style == null) {
            return;
        }
        this.orientation = style.orientation;
        this.defaultForegroundColor = style.defaultForegroundColor;
        this.defaultBackgroundColor = style.defaultBackgroundColor;
        this.selectedForegroundColor = style.selectedForegroundColor;
        this.selectedBackgroundColor = style.selectedBackgroundColor;
        this.selectedSymbol = style.selectedSymbol;
        this.padding = style.padding;
    }

    /**
     * Sets the colors of the menu to the provided colors. If any arguments are null, the respective
     * color is not changed.
     *
     * @param defaultForegroundColor  the color of the menu text when not selected; can be null
     * @param defaultBackgroundColor  the color underneath the menu text when not selected; can be
     *                                null
     * @param selectedForegroundColor the color of the menu text when selected; can be null
     * @param selectedBackgroundColor the color underneath the menu text when selected; can be null
     */
    public void setColors(
        @Nullable ColorCode defaultForegroundColor,
        @Nullable ColorCode defaultBackgroundColor,
        @Nullable ColorCode selectedForegroundColor,
        @Nullable ColorCode selectedBackgroundColor) {
        if (defaultForegroundColor != null) {
            this.defaultForegroundColor = defaultForegroundColor;
        }
        if (defaultBackgroundColor != null) {
            this.defaultBackgroundColor = defaultBackgroundColor;
        }
        if (selectedForegroundColor != null) {
            this.selectedForegroundColor = selectedForegroundColor;
        }
        if (selectedBackgroundColor != null) {
            this.selectedBackgroundColor = selectedBackgroundColor;
        }
    }

    /**
     * Sets the padding to the provided padding amount. The padding is the amount of blank rows or
     * columns separating menu options.
     *
     * @param padding the new value for padding
     */
    public void setPadding(int padding) {
        this.padding = padding;
    }

    /**
     * Sets the orientation to the provided orientation. The orientation determines if the menu
     * should be rendered vertically or horizontally.
     *
     * @param orientation the new orientation
     */
    public void setOrientation(MenuOrientation orientation) {
        if (orientation != null) {
            this.orientation = orientation;
        }
    }

    /**
     * Adds an option to the menu at the end, eligible for selection.
     *
     * @param option the new option
     */
    public void add(String option) {
        if (option != null) {
            this.options.put(this.options.size(), option);
        }
    }

    /**
     * Selects the next option down the list if there is one.
     */
    public void next() {
        if (hasNext()) {
            selectedIndex++;
        }
    }

    /**
     * Determines if there is an option after the currently selected one and returns the result.
     *
     * @return {@code true} if there is an option after, {@code false} if the last option is
     * selected
     */
    public boolean hasNext() {
        return this.selectedIndex < this.options.size() - 1;
    }

    /**
     * Selects the previous option up the list if there is one.
     */
    public void previous() {
        if (hasPrevious()) {
            selectedIndex--;
        }
    }

    /**
     * Determines if there is an option before the currently selected one and returns the result.
     *
     * @return {@code true} if there is an option before, {@code false} if the first option is
     * selected
     */
    public boolean hasPrevious() {
        return this.selectedIndex > 0;
    }

    /**
     * Returns the string ID of the currently selected option.
     * @return the selected option’s string ID
     */
    public String getSelectedOption() {
        return this.options.get(this.selectedIndex);
    }

    @Override
    public void onRender() {
        if (this.options.size() == 0) {
            this.screen.putAll(Drawable.fromString("empty menu", this.origin, true));
        }
        boolean hasSymbol = this.selectedSymbol != ' ';
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.options.size(); i++) {

            if (this.selectedIndex == i) {
                builder.append(selectedForegroundColor.fgColor());
                builder.append(selectedBackgroundColor.bgColor());
            } else {
                builder.append(defaultForegroundColor.fgColor());
                builder.append(defaultBackgroundColor.bgColor());
            }

            if (hasSymbol) {
                builder.append(this.selectedIndex == i ? this.selectedSymbol : ' ').append(" ");
            }

            builder.append(this.options.get(i));

            switch (this.orientation) {
                case VERTICAL -> builder.append("\n".repeat(1 + this.padding));
                case HORIZONTAL -> builder.append(" ".repeat(1 + this.padding));
            }
        }

        this.screen.putAll(Drawable.fromString(builder.toString(), this.origin, false));
    }

    @Override
    public int rows() {
        if (this.orientation == MenuOrientation.HORIZONTAL) {
            return 1;
        } else {
            return this.options.size() + this.padding * (this.options.size() - 1);
        }
    }

    @Override
    public int columns() {
        int width = 0;
        for (String option : this.options.values()) {
            if (this.orientation == MenuOrientation.HORIZONTAL) {
                width += Drawable.removeColorCodes(option).length();
            } else {
                width = Math.max(width, Drawable.removeColorCodes(option).length());
            }
        }
        width += this.selectedSymbol != ' ' ? 2 : 0;
        if (this.orientation == MenuOrientation.VERTICAL) {
            int longestOption = 0;
            for (String option : this.options.values()) {
                longestOption = Math.max(longestOption, option.length());
            }
            width += longestOption;
        } else {
            width += this.options.size() - 1;
        }
        return width;
    }
}
