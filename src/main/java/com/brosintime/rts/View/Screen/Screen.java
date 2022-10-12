package com.brosintime.rts.View.Screen;

import com.brosintime.rts.Controller.GameController;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.View.Cell;
import com.brosintime.rts.View.GameView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public abstract class Screen implements Focusable, Drawable {

    protected GameController controller;
    protected GameView client;
    protected List<Drawable> children = new ArrayList<>();
    protected Map<Node, Cell> screen = new HashMap<>();
    protected Node origin;
    protected int width = 1;
    protected int height = 1;
    protected Drawable parent = null;
    protected boolean visible = true;
    protected boolean justHidden = false;

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    @Override
    public Node origin() {
        return this.origin;
    }

    @Override
    public void onHidden() {
        this.justHidden = false;
        this.screen.putAll(Drawable.blankScreen(this));
    }

    @Override
    public boolean justHidden() {
        return this.justHidden;
    }

    @Override
    public int columns() {
        return this.width;
    }

    @Override
    public int rows() {
        return this.height;
    }

    @Override
    public Map<Node, Cell> toCells() {
        if (this.justHidden) {
            this.onHidden();
        } else if (this.visible) {
            this.onRender();
        }
        Map<Node, Cell> cells = new HashMap<>(this.screen);
        if (this.hasChildren()) {
            for (Drawable screen : this.children) {
                if (screen.visible() || screen.justHidden()) {
                    cells.putAll(screen.toCells());
                }
            }
        }
        return cells;
    }

    @Override
    public void setParent(@Nullable Drawable parent) {
        if (this.parent != null) {
            this.parent.children().remove(this);
        }
        this.parent = parent;
        if (this.parent != null) {
            this.parent.children().add(this);
        }
    }

    @Override
    public List<Drawable> children() {
        return this.children;
    }

    @Override
    public Drawable parent() {
        return this.parent;
    }

    @Override
    public void close() {
        this.client.clear();
        if (this.hasParent()) {
            this.parent().children().remove(this);
            this.client.setScreen(this.parent);
        } else {
            this.client.setScreen(null);
        }
    }

    @Override
    public Drawable firstAncestor() {
        if (this.hasParent()) {
            return this.parent.firstAncestor();
        } else {
            return this;
        }
    }

    @Override
    public boolean visible() {
        return this.visible;
    }

    @Override
    public void show() {
        this.visible = true;
    }

    @Override
    public void hide() {
        if (this.visible) {
            this.justHidden = true;
        }
        this.visible = false;
    }

    @Override
    public Drawable asHidden() {
        this.visible = false;
        return this;
    }

    @Override
    public Drawable asChild(Drawable parent) {
        this.setParent(parent);
        return this;
    }

}
