package com.brosintime.rts.Model;

/**
 * The Node class is composed of a row and column that identify where the node is on the Board.
 */
public record Node(int row, int column) {

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
        String s = "";
        s = s + row;
        s = s + column;
        return s;
    }
}
