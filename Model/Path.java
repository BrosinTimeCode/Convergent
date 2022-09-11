package Model;

import java.lang.reflect.Array;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Objects;

// A path contains nodes and goes from one location to another
public class Path {

    public ArrayList<Node> path;

    public Path() {
        path = new ArrayList<>();
    }

    public void addNode(int row, int column) {
        path.add(new Node(row, column));
    }

    public Node getLast() {
        return path.get(path.size() - 1);
    }

    public Node getFirst() {
        return path.get(0);
    }

    public void appendPath(Path path) {
        this.path.addAll(path.path);
    }

    public int getLength() {
        return path.size();
    }

    public ArrayList<Node> getReversePath() {
        ArrayList<Node> reversePath = new ArrayList<>();
        ListIterator<Node> iterator = path.listIterator(path.size());
        while(iterator.hasPrevious()) {
            reversePath.add(iterator.previous());
        }
        return reversePath;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Path p)) {
            return false;
        }
        return this.path.equals(p.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path);
    }
}
