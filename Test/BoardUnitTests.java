package Test;

import Model.Board;
import Model.BoardCell;
import Model.Node;
import Units.BaseUnit;
import Units.Civilian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardUnitTests {
    private Board board;
    @BeforeEach
    void initAll() {
        board = new Board(10,10);
    }
    @Test
    void testNextNode() {
        // Direct diagonal up-left
        assertEquals(new Node(4, 4), board.nextNode(5, 5, 0, 0));
        // Direct diagonal up-right
        assertEquals(new Node(4, 6), board.nextNode(5, 5, 0, 10));
        // Direct diagonal bottom-left
        assertEquals(new Node(6, 4),board.nextNode(5, 5, 10, 0));
        // Direct diagonal bottom-right
        assertEquals(new Node(6, 6),board.nextNode(5, 5, 10, 10));
        // Direct up
        assertEquals(new Node (4, 5),board.nextNode(5, 5, 0, 5));
        // Direct right
        assertEquals(new Node (5, 6),board.nextNode(5, 5, 5, 10));
        // Direct down
        assertEquals(new Node(6, 5),board.nextNode(5, 5, 10, 5));
        // Direct left
        assertEquals(new Node(5, 4),board.nextNode(5, 5, 5, 0));

        // TODO: add non straight lines between two point test cases
    }
    @Test
    void testNextNodeWithEnemy() {
        // TODO: add test cases for not going into enemy units
        /* 0---0
           -X-X-
           --0--
           -X-X-
           0---0 */
        board.board[0][0] = new BoardCell(new Civilian(BaseUnit.Team.RED));
        board.board[0][4] = new BoardCell(new Civilian(BaseUnit.Team.RED));
        board.board[4][0] = new BoardCell(new Civilian(BaseUnit.Team.RED));
        board.board[4][4] = new BoardCell(new Civilian(BaseUnit.Team.RED));
        board.board[2][2] = new BoardCell(new Civilian(BaseUnit.Team.RED));
        board.board[1][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE));
        board.board[1][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE));
        board.board[3][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE));
        board.board[3][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE));
        // Direct diagonal up-right
        assertEquals()
    }

}
