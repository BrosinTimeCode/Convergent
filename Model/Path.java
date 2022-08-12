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
}
