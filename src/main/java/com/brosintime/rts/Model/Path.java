package com.brosintime.rts.Model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The Path class is a representation of a path from one location to another. The path contains
 * nodes from one location to the other.
 */
public class Path {

    public ArrayList<Node> path;

    public Path() {
        path = new ArrayList<>();
    }

    /**
     * Adds node to path.
     *
     * @param row    An integer representing the row of the added node.
     * @param column An integer representing the column of the added node.
     */
    public void addNode(int row, int column) {
        path.add(new Node(row, column));
    }

    /**
     * Returns the node at the end of the Path.
     *
     * @return Last Node in the Path.
     */
    public Node getLast() {
        return path.get(path.size() - 1);
    }

    /**
     * Returns the node at the start of the Path.
     *
     * @return First Node in the Path.
     */
    public Node getFirst() {
        return path.get(0);
    }

    /**
     * Adds the path's nodes to the end of this instance's path.
     *
     * @param path Path to be added to path.
     */
    public void appendPath(Path path) {
        this.path.addAll(path.path);
    }

    public int getLength() {
        return path.size();
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
