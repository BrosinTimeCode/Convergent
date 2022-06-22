package Model;

public class Node {
    int row;
    int column;
    public Node(int row, int column){
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof Node)) {
            return false;
        }
        Node n = (Node) o;
        return this.row == n.row && this.column == n.column;
    }
}
