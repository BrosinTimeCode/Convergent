package Model;

public class Board extends BaseBoard {
    public BoardCell[][] board;
    public int length;
    public int height;

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
    public Path pathFinder(int rowStart, int columnStart, int rowEnd, int columnEnd) {
        Path path = new Path();
        Node pathNode = nextNode(rowStart, columnStart, rowEnd, columnEnd);
        while(pathNode != null) {
            path.addNode(pathNode.getRow(), pathNode.getColumn());
            pathNode = nextNode(pathNode.getRow(), pathNode.getColumn(), rowEnd, columnEnd);
        }
        return path;
    }

    // Gives the next closest node to the current that goes towards end node
    public Node nextNode(int rowCurrent, int columnCurrent, int rowEnd, int columnEnd) {
        // Diagonal path towards destination
        if(rowCurrent != rowEnd && columnCurrent != columnEnd) {
            // Top left
            if(rowCurrent > rowEnd && columnCurrent > columnEnd) {
                return new Node(rowCurrent - 1, columnCurrent - 1);
            }
            // Top right
            else if(rowCurrent > rowEnd && columnCurrent < columnEnd) {
                return new Node(rowCurrent - 1, columnCurrent + 1);
            }
            // Bottom left
            else if(rowCurrent < rowEnd && columnCurrent > columnEnd) {
                return new Node(rowCurrent + 1, columnCurrent - 1);
            }
            // Bottom right
            else if((rowCurrent < rowEnd && columnCurrent < columnEnd)) {
                return new Node(rowCurrent + 1, columnCurrent + 1);
            }
        }
        // Current is in the correct column
        // Up
        else if(rowCurrent != rowEnd && rowEnd > rowCurrent) {
            return new Node(rowCurrent + 1, columnCurrent);
        }
        // Down
        else if(rowCurrent != rowEnd && rowEnd < rowCurrent) {
            return new Node(rowCurrent - 1, columnCurrent);
        }
        // Current is in the correct row
        // Left
        else if(columnCurrent != columnEnd && columnEnd < columnCurrent) {
            return new Node(rowCurrent, columnCurrent - 1);
        }
        // Right
        else if(columnCurrent != columnEnd && columnEnd > columnCurrent) {
            return new Node(rowCurrent, columnCurrent + 1);
        }
        // Current equals End
        return null;
    }

}
