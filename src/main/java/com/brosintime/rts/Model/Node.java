package com.brosintime.rts.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The Node class is composed of a row and column that identify where the node is on the Board.
 */
public record Node(int row, int column) {

    public enum Bounds {
        MIN_X,
        MIN_Y,
        MAX_X,
        MAX_Y
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Node n)) {
            return false;
        }
        return this.row == n.row && this.column == n.column;
    }

    @Override
    public String toString() {
        return "(" + this.column + ", " + this.row + ")";
    }

    public static Map<Bounds, Integer> bounds(Set<Node> nodes) {
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        for (Node node : nodes) {
            minX = Math.min(minX, node.column);
            minY = Math.min(minY, node.row);
            maxX = Math.max(maxX, node.column);
            maxY = Math.max(maxY, node.row);
        }
        Map<Bounds, Integer> bounds = new HashMap<>();
        bounds.put(Bounds.MIN_X, minX);
        bounds.put(Bounds.MIN_Y, minY);
        bounds.put(Bounds.MAX_X, maxX);
        bounds.put(Bounds.MAX_Y, maxY);
        return bounds;
    }

    public static Node relativeTo(Node origin, int row, int column) {
        return new Node(origin.row() + row, origin.column() + column);
    }
}