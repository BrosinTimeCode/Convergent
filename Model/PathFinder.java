package Model;

import Units.BaseUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class PathFinder {

    public BoardCell[][] board;

    public PathFinder(BoardCell[][] board) {
        this.board = board;
    }

    // Pathfinding.
    public Path pathFinder(int rowStart, int columnStart, int rowEnd, int columnEnd,
      BaseUnit.Team team, HashMap<Node, Boolean> exploredNodes) {
        Path path = new Path();
        if (!checkBounds(rowStart, columnStart, rowEnd, columnEnd)) {
            return null;
        }
        Node pathNode = nextClosestNode(rowStart, columnStart, rowEnd, columnEnd);
        while (pathNode != null) {
            // The next location has a unit that is not on the same team
            if (!checkTeam(board[pathNode.getRow()][pathNode.getColumn()], team)
              || exploredNodes.containsKey(new Node(
              pathNode.getRow(), pathNode.getColumn()))) {
                Path nonEnemyPath;
                if (path.getLength() == 0) {
                    nonEnemyPath = nonEnemyPath(rowStart, columnStart,
                      rowEnd, columnEnd, team, exploredNodes);
                } else {
                    nonEnemyPath = nonEnemyPath(path.getLast().getRow(), path.getLast().getColumn(),
                      rowEnd, columnEnd, team, exploredNodes);
                }
                if (nonEnemyPath != null) {
                    path.appendPath(nonEnemyPath);
                } else {
                    path = null;
                }
                break;
            }
            path.addNode(pathNode.getRow(), pathNode.getColumn());
            pathNode = nextClosestNode(pathNode.getRow(), pathNode.getColumn(), rowEnd, columnEnd);
        }
        return path;
    }

    public boolean checkBounds(int rowStart, int columnStart, int rowEnd, int columnEnd) {
        return rowStart > 0 && rowStart < board.length && columnStart > 0
                && columnStart < board[0].length
                && rowEnd > 0 && rowEnd < board.length && columnEnd > 0 && columnEnd < board[0].length;
    }

    // Gives the next closest node to the current that goes towards end node
    public Node nextClosestNode(int rowCurrent, int columnCurrent, int rowEnd, int columnEnd) {
        // Diagonal path towards destination
        if (rowCurrent != rowEnd && columnCurrent != columnEnd) {
            // Top left
            if (rowCurrent > rowEnd && columnCurrent > columnEnd) {
                return new Node(rowCurrent - 1, columnCurrent - 1);
            }
            // Top right
            else if (rowCurrent > rowEnd) {
                return new Node(rowCurrent - 1, columnCurrent + 1);
            }
            // Bottom left
            else if (columnCurrent > columnEnd) {
                return new Node(rowCurrent + 1, columnCurrent - 1);
            }
            // Bottom right
            else {
                return new Node(rowCurrent + 1, columnCurrent + 1);
            }
        }
        // Current is in the correct column
        // Up
        else if (rowEnd > rowCurrent) {
            return new Node(rowCurrent + 1, columnCurrent);
        }
        // Down
        else if (rowEnd < rowCurrent) {
            return new Node(rowCurrent - 1, columnCurrent);
        }
        // Current is in the correct row
        // Left
        else if (columnEnd < columnCurrent) {
            return new Node(rowCurrent, columnCurrent - 1);
        }
        // Right
        else if (columnEnd > columnCurrent) {
            return new Node(rowCurrent, columnCurrent + 1);
        }
        // Current equals End
        return null;
    }

    public Path nonEnemyPath(int rowCurrent, int columnCurrent, int rowEnd, int columnEnd,
      BaseUnit.Team team, HashMap<Node, Boolean> exploredNodes) {
        exploredNodes.put(new Node(rowCurrent, columnCurrent), true);
        ArrayList<Node> adjacentNodes = getValidAdjacentNodes(rowCurrent, columnCurrent, team,
          exploredNodes);
        ArrayList<Path> adjacentPaths = new ArrayList<>();
        for (Node node : adjacentNodes) {
            Path path = new Path();
            path.addNode(node.getRow(), node.getColumn());
            Path nextPath = pathFinder(node.getRow(), node.getColumn(), rowEnd, columnEnd, team,
              exploredNodes);
            if (nextPath != null) {
                path.appendPath(nextPath);
            }
            adjacentPaths.add(path);
        }
        Path shortestValidPath = null;
        for (Path path : adjacentPaths) {
            if (shortestValidPath == null && path.getLast().getRow() == rowEnd
              && path.getLast().getColumn() == columnEnd) {
                shortestValidPath = path;
            } else if (shortestValidPath != null && path.getLength() < shortestValidPath.getLength()
              && path.getLast().getRow() == rowEnd
              && path.getLast().getColumn() == columnEnd) {
                shortestValidPath = path;
            }
        }
        // No valid path between two nodes, choose the path that gets the closest to ending node
        if(shortestValidPath == null && !adjacentPaths.isEmpty()) {
            shortestValidPath = adjacentPaths.get(0);
            for(Path path: adjacentPaths) {
                if(distanceBetweenNodes(path.getLast().getRow(), path.getLast().getColumn(), rowEnd, columnEnd) < distanceBetweenNodes(shortestValidPath.getLast().getRow(), shortestValidPath.getLast().getColumn(), rowEnd, columnEnd)) {
                    shortestValidPath = path;
                }
            }
        }
        return shortestValidPath;
    }

     public int distanceBetweenNodes(int row1, int column1, int row2, int column2) {
        int columnDistance = Math.abs(column1 - column2);
        int rowDistance = Math.abs(row1 - row2);
         return Math.max(columnDistance, rowDistance);
     }

    public boolean checkTeam(BoardCell cell1, BaseUnit.Team team) {
        // If there is no cell in the board than it is a square that can be traversed
        if (cell1.unit == null) {
            return true;
        } else {
            return cell1.unit.getTeam() == team;
        }
    }

    public ArrayList<Node> getValidAdjacentNodes(int rowCurrent, int columnCurrent,
      BaseUnit.Team team, HashMap<Node, Boolean> exploredNodes) {
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Node> validNodes = new ArrayList<>();
        // Diagonal Up-Left
        nodes.add(new Node(rowCurrent - 1, columnCurrent - 1));
        // Up
        nodes.add(new Node(rowCurrent - 1, columnCurrent));
        // Diagonal Up-Right
        nodes.add(new Node(rowCurrent - 1, columnCurrent + 1));
        // Left
        nodes.add(new Node(rowCurrent, columnCurrent - 1));
        // Right
        nodes.add(new Node(rowCurrent, columnCurrent + 1));
        // Diagonal Down-Left
        nodes.add(new Node(rowCurrent + 1, columnCurrent - 1));
        // Down
        nodes.add(new Node(rowCurrent + 1, columnCurrent));
        // Diagonal Down-Right
        nodes.add(new Node(rowCurrent + 1, columnCurrent + 1));
        for (Node node : nodes) {
            if (node.getRow() >= 0 && node.getRow() < board.length && node.getColumn() >= 0 &&
              node.getColumn() < board[0].length && checkTeam(
              board[node.getRow()][node.getColumn()],
              team)
              && !exploredNodes.containsKey(node)) {
                validNodes.add(node);
                exploredNodes.put(node, true);
            }
        }
        return validNodes;
    }


}
