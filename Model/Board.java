package Model;

import Units.BaseUnit;

import java.util.ArrayList;

public class Board extends BaseBoard {
    public BoardCell[][] board;

    public Board(int row, int column) {
        board = new BoardCell[row][column];
    }

    public Board() {
     }
    @Override
    // toString method used for printing the board
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(BoardCell[] row : board) {
            for(BoardCell column : row) {
                // If there is no unit in cell
                if(column.unit == null) {
                    builder.append("0");
                }
                else {
                    builder.append(column.unit.getName());
                }
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    // Pathfinding. First iteration units cannot go through occupied squares
    //TODO: Add ability to go through ally squares while restricting going through enemy
    public Path pathFinder(int rowStart, int columnStart, int rowEnd, int columnEnd, BaseUnit.Team team) {
        Path path = new Path();
        Node pathNode = nextNode(rowStart, columnStart, rowEnd, columnEnd);
        while(pathNode != null) {
            // The next location has a unit that is not on the same team
            if(!checkTeam(board[pathNode.getRow()][pathNode.getColumn()], team)){
                Path nonEnemyPath  = nonEnemyPath(path.getLast().getRow(), path.getLast().getColumn(), rowEnd, columnEnd, team);
                path.appendPath(nonEnemyPath);
                break;
            }
            path.addNode(pathNode.getRow(), pathNode.getColumn());
            pathNode = nextNode(pathNode.getRow(), pathNode.getColumn(), rowEnd, columnEnd);
        }

        // TODO: If ending square is an enemy or ally do not go into, make it to the next habitable square
        return path;
    }

    // Gives the next closest node to the current that goes towards end node
    // TODO: Keep similar algorithm, but add recursion. If next node is an enemy find alternate node.
    // TODO: When climbing out of recursion begin to populate the container with the path
    public Node nextNode(int rowCurrent, int columnCurrent, int rowEnd, int columnEnd) {
        // Diagonal path towards destination
        if(rowCurrent != rowEnd && columnCurrent != columnEnd) {
            // Top left
            if(rowCurrent > rowEnd && columnCurrent > columnEnd) {
                return new Node(rowCurrent - 1, columnCurrent - 1);
            }
            // Top right
            else if(rowCurrent > rowEnd) {
                return new Node(rowCurrent - 1, columnCurrent + 1);
            }
            // Bottom left
            else if(columnCurrent > columnEnd) {
                return new Node(rowCurrent + 1, columnCurrent - 1);
            }
            // Bottom right
            else {
                return new Node(rowCurrent + 1, columnCurrent + 1);
            }
        }
        // Current is in the correct column
        // Up
        else if(rowEnd > rowCurrent) {
            return new Node(rowCurrent + 1, columnCurrent);
        }
        // Down
        else if(rowEnd < rowCurrent) {
            return new Node(rowCurrent - 1, columnCurrent);
        }
        // Current is in the correct row
        // Left
        else if(columnEnd < columnCurrent) {
            return new Node(rowCurrent, columnCurrent - 1);
        }
        // Right
        else if(columnEnd > columnCurrent) {
            return new Node(rowCurrent, columnCurrent + 1);
        }
        // Current equals End
        return null;
    }

    public Path nonEnemyPath(int rowCurrent, int columnCurrent, int rowEnd, int columnEnd, BaseUnit.Team team) {
        return null;
    }

    public boolean checkTeam(BoardCell cell1, BaseUnit.Team team) {
        // If there is no cell in the board than it is a square that can be traversed
        if(cell1.unit == null) {
            return true;
        }
        else return cell1.unit.getTeam() == team;
    }

}
