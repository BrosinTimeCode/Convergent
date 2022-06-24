package Model;

public class Board extends BaseBoard {
    public BoardCell[][] board;
    public int length;
    public int height;

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
    public void pathFinder(int rowStart, int columnStart, int rowEnd, int columnEnd) {

    }

    public Node nextNode(int rowCurrent, int columnCurrent, int rowEnd, int columnEnd) {
        return null;
    }

}
