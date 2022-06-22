package Test;

import Model.Board;
import static org.junit.jupiter.api.Assertions.assertEquals;

import Model.Node;
import org.junit.jupiter.api.Test;

public class BoardUnitTests {
    private final Board board = new Board();

    @Test
    void testNextNode() {
        // Direct diagonal up-left
        assertEquals(new Node(4,6), board.nextNode(5, 5, 0, 10));
        // Direct diagonal up-right
        // Direct diagonal bottom-left
        // Direct diagonal bottom-right
    }
}
