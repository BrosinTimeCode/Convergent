package Model;

import Units.BaseUnit;

import java.util.ArrayList;
import java.util.HashMap;

public class Board extends BaseBoard {

    public BoardCell[][] board;

    public Board(int row, int column) {
        board = new BoardCell[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board[i][j] = new BoardCell(null);
            }
        }
    }

    public Board() {
    }

    @Override
    // toString method used for printing the board
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (BoardCell[] row : board) {
            for (BoardCell column : row) {
                // If there is no unit in cell
                if (column.unit == null) {
                    builder.append("0");
                } else {
                    builder.append(column.unit.getName());
                }
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    // Pathfinding.
    public Path pathFinder(int rowStart, int columnStart, int rowEnd, int columnEnd,
      BaseUnit.Team team, HashMap<Node, Boolean> exploredNodes) {
        Path path = new Path();
        if (rowStart < 0 || rowStart > board.length || columnStart < 0
          || columnStart > board[0].length
          || rowEnd < 0 || rowEnd > board.length || columnEnd > board[0].length) {
            return null;
        }
        Node pathNode = nextNode(rowStart, columnStart, rowEnd, columnEnd);
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
            pathNode = nextNode(pathNode.getRow(), pathNode.getColumn(), rowEnd, columnEnd);
        }
        return path;
    }

    // Gives the next closest node to the current that goes towards end node
    public Node nextNode(int rowCurrent, int columnCurrent, int rowEnd, int columnEnd) {
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
            } else {
                continue;
            }
            adjacentPaths.add(path);
        }
        Path shortestPath = null;
        for (Path path : adjacentPaths) {
            if (shortestPath == null && path.getLast().getRow() == rowEnd
              && path.getLast().getColumn() == columnEnd) {
                shortestPath = path;
            } else if (shortestPath != null && path.getLength() < shortestPath.getLength()
              && path.getLast().getRow() == rowEnd
              && path.getLast().getColumn() == columnEnd) {
                shortestPath = path;
            }
        }
        return shortestPath;
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
