package Model;

public class Board extends BaseBoard {
    public BoardCell[][] board;

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
}
