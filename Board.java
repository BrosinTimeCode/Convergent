import java.util.Random;

public class Board {
    public BoardCell[][] board;
    // Random board generator for testing purposes
    public Board(int random, int x, int y) {
        Random randomGenerator = new Random(random);
        board = new BoardCell[x][y];
        for(int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                int value = randomGenerator.nextInt();
                if(value < 0) value = value * -1;
                value = value % 26;
                value = value + 65;
                char unitName = (char) value;
                board[row][column] = new BoardCell(new DummyUnit(unitName));
            }
        }

    }
    @Override
    // toString method used for printing the board
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(BoardCell[] row : board) {
            for(BoardCell column : row) {
                builder.append(column.unit.name);
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
