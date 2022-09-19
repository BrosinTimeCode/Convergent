package Model;

import Units.BaseUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class PathFinder {

    public BoardCell[][] board;

    public PathFinder(BoardCell[][] board) {
        this.board = board;
    }

    /**
     * Finds a path between the starting node and the ending node.
     *
     * @param rowStart      An integer representing the row of the location of the starting node.
     * @param columnStart   An integer representing the column of the location of the starting
     *                      node.
     * @param rowEnd        An integer representing the row of the location of the ending node.
     * @param columnEnd     An integer representing the column of the location of the ending node.
     * @param team          The BaseUnit.Team that the pathing unit is a part of.
     * @param exploredNodes The nodes that have already been visited during pathing.
     * @return A Path from starting node and ending node. If no path found returns null.
     */
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

    /**
     * Checks the starting location and ending location and determines if they are in the bounds of
     * the board.
     *
     * @param rowStart    An integer representing the row of the location of the start node.
     * @param columnStart An integer representing the column of the location of the start node.
     * @param rowEnd      An integer representing the row of the location of the end node.
     * @param columnEnd   An integer representing the column of the location of the end node.
     * @return A boolean showing if two nodes are in bounds of the board.
     */
    private boolean checkBounds(int rowStart, int columnStart, int rowEnd, int columnEnd) {
        return rowStart >= 0 && rowStart < board.length && columnStart >= 0
            && columnStart < board[0].length
            && rowEnd >= 0 && rowEnd < board.length && columnEnd >= 0
            && columnEnd < board[0].length;
    }

    /**
     * Gives the next closest node to the current node that moves closer to end node.
     *
     * @param rowCurrent    An integer representing the row of the location of the current node.
     * @param columnCurrent An integer representing the column of the location of the current node.
     * @param rowEnd        An integer representing the row of the location of the destination
     *                      node.
     * @param columnEnd     An integer representing the column of the location of the destination
     *                      node.
     * @return A Node that moves optimally from current position to ending position.
     */
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

    /**
     * Finds a path between the current node and the ending node if there is an enemy unit in the
     * location that is the optimal path.
     *
     * @param rowCurrent    An integer representing the row of the location of the current node.
     * @param columnCurrent An integer representing the column of the location of the current node.
     * @param rowEnd        An integer representing the row of the location of the ending node.
     * @param columnEnd     An integer representing the column of the location of the ending node.
     * @param team          The BaseUnit.Team that the pathing unit is a part of.
     * @param exploredNodes The nodes that have already been visited during pathing.
     * @return A Path from current node and ending node. If no path found returns null.
     */
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
        if (shortestValidPath == null && !adjacentPaths.isEmpty()) {
            shortestValidPath = adjacentPaths.get(0);
            for (Path path : adjacentPaths) {
                if (
                    distanceBetweenNodes(path.getLast().getRow(), path.getLast().getColumn(),
                        rowEnd,
                        columnEnd) < distanceBetweenNodes(shortestValidPath.getLast().getRow(),
                        shortestValidPath.getLast().getColumn(), rowEnd, columnEnd)) {
                    shortestValidPath = path;
                }
            }
        }
        return shortestValidPath;
    }

    /**
     * Calculates the distance between two nodes. Distance is determined based on a distance of one
     * away from all eight adjacent nodes. Diagonal nodes are a distance of one just like orthogonal
     * nodes.
     *
     * @param row1    An integer representing the row of location of the first node.
     * @param column1 An integer representing the column of location of the first node.
     * @param row2    An integer representing the row of location of the second node.
     * @param column2 An integer representing the column of location of the second node.
     * @return An integer value of the distance between two nodes.
     */
    public int distanceBetweenNodes(int row1, int column1, int row2, int column2) {
        int columnDistance = Math.abs(column1 - column2);
        int rowDistance = Math.abs(row1 - row2);
        return Math.max(columnDistance, rowDistance);
    }

    /**
     * Checks if BoardCell contains a unit that is on the same team as unit that is pathing.
     *
     * @param cell1 BoardCell holding unit to check team.
     * @param team  Team of unit that is pathing.
     * @return A boolean showing if BoardCell's unit is on the same team as pathing unit.
     */
    private boolean checkTeam(BoardCell cell1, BaseUnit.Team team) {
        // If there is no cell in the board than it is a square that can be traversed
        if (cell1.unit == null) {
            return true;
        } else {
            return cell1.unit.getTeam() == team;
        }
    }

    /**
     * Identifies valid adjacent nodes that path can move into.
     *
     * @param rowCurrent    An integer representing the row path is at.
     * @param columnCurrent An integer representing the column path is at.
     * @param team          Team of unit that is pathing.
     * @param exploredNodes Nodes that pathfinder has already checked.
     * @return A List of adjacent Nodes that are valid.
     */
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
