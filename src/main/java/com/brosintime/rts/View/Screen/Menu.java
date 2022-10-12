package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Model.Node;
import com.brosintime.rts.View.GameView;
import com.googlecode.lanterna.input.KeyStroke;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

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

    public enum MenuOrientation {
        HORIZONTAL,
        VERTICAL
    }

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

    public Menu(GameView client, Node origin, MenuStyle style) {

        if (client == null) {
            throw new IllegalArgumentException("Menu created with null game client");
        }
        this.client = client;
        this.origin = origin != null ? origin : new Node(0, 0);
        this.setStyle(style);

    }

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

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setOrientation(MenuOrientation orientation) {
        if (orientation != null) {
            this.orientation = orientation;
        }
    }

    public void add(String option) {
        if (option != null) {
            this.options.put(this.options.size(), option);
        }
    }

    public void next() {
        if (hasNext()) {
            selectedIndex++;
        }
    }

    public boolean hasNext() {
        return this.selectedIndex < this.options.size() - 1;
    }

    public void previous() {
        if (hasPrevious()) {
            selectedIndex--;
        }
    }

    public boolean hasPrevious() {
        return this.selectedIndex > 0;
    }

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
