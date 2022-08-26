package Model;

import java.util.ArrayList;

// A path contains nodes and goes from one location to another
public class Path {

    public ArrayList<Node> path;

    public Path() {
        path = new ArrayList<>();
    }

    public void addNode(int row, int column) {
        path.add(new Node(row, column));
    }
}
