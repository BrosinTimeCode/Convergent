package Model;

import java.util.Objects;

public class Node {
    private int row;
    private int column;
    public Node(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(o == null) {
            return false;
        }
        if(!(o instanceof Node)) {
            return false;
        }
        Node n = (Node) o;
        return this.row == n.row && this.column == n.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        String s = "";
        s = s + row;
        s = s + column;
        return s;
    }
}
