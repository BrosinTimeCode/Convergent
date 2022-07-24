package Test;

import Model.Board;
import static org.junit.jupiter.api.Assertions.assertEquals;

import Model.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        board[1][1]
    }

}
