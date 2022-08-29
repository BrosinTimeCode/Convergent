package Test;

import Model.Board;
import Model.BoardCell;
import Model.Node;
import Model.Path;
import Model.PathFinder;
import Units.BaseUnit;
import Units.BaseUnit.Team;
import Units.Civilian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PathFinderUnitTests {

    private PathFinder pathFinder;

    @BeforeEach
    void initAll() {
        Board board = new Board(5, 10);
        pathFinder = new PathFinder(board.board);
    }

    @Test
    void testNextNode() {
        // Direct diagonal up-left
        assertEquals(new Node(4, 4), pathFinder.nextNode(5, 5, 0, 0));
        // Direct diagonal up-right
        assertEquals(new Node(4, 6), pathFinder.nextNode(5, 5, 0, 10));
        // Direct diagonal bottom-left
        assertEquals(new Node(6, 4), pathFinder.nextNode(5, 5, 10, 0));
        // Direct diagonal bottom-right
        assertEquals(new Node(6, 6), pathFinder.nextNode(5, 5, 10, 10));
        // Direct up
        assertEquals(new Node(4, 5), pathFinder.nextNode(5, 5, 0, 5));
        // Direct right
        assertEquals(new Node(5, 6), pathFinder.nextNode(5, 5, 5, 10));
        // Direct down
        assertEquals(new Node(6, 5), pathFinder.nextNode(5, 5, 10, 5));
        // Direct left
        assertEquals(new Node(5, 4), pathFinder.nextNode(5, 5, 5, 0));

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
        pathFinder.board[0][0] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[0][4] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[4][0] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[4][4] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[2][2] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[1][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[3][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[3][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        // Diagonal up-left
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 0, BaseUnit.Team.RED, new HashMap<>()).getFirst(),
          (new Node(1, 2)));
        // Diagonal up-right
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 4, BaseUnit.Team.RED, new HashMap<>()).getFirst(),
          (new Node(1, 2)));
        // Diagonal bottom-left
        assertEquals(pathFinder.nonEnemyPath(2, 2, 4, 0, BaseUnit.Team.RED, new HashMap<>()).getFirst(),
          (new Node(2, 1)));
        // Diagonal bottom-right
        assertEquals(pathFinder.nonEnemyPath(2, 2, 4, 4, BaseUnit.Team.RED, new HashMap<>()).getFirst(),
          (new Node(2, 3)));
        /* 0---0
           -XXX-
           --0--
           -XXX-
           0---0 */
        pathFinder.board[1][2] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[3][2] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        // Diagonal up-left
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 0, BaseUnit.Team.RED, new HashMap<>()).getFirst(),
          (new Node(2, 1)));
        // Diagonal up-right
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 4, BaseUnit.Team.RED, new HashMap<>()).getFirst(),
          (new Node(2, 3)));
        // Diagonal bottom-left
        assertEquals(pathFinder.nonEnemyPath(2, 2, 4, 0, BaseUnit.Team.RED, new HashMap<>()).getFirst(),
          (new Node(2, 1)));
        // Diagonal bottom-right
        assertEquals(pathFinder.nonEnemyPath(2, 2, 4, 4, BaseUnit.Team.RED, new HashMap<>()).getFirst(),
          (new Node(2, 3)));
    }

    @Test
    void testGetValidAdjacentNodes() {
        ArrayList<Node> everyAdjacent = new ArrayList<>();
        everyAdjacent.add(new Node(1, 1));
        everyAdjacent.add(new Node(1, 2));
        everyAdjacent.add(new Node(1, 3));
        everyAdjacent.add(new Node(2, 1));
        everyAdjacent.add(new Node(2, 3));
        everyAdjacent.add(new Node(3, 1));
        everyAdjacent.add(new Node(3, 2));
        everyAdjacent.add(new Node(3, 3));
        ArrayList<Node> emptyList = new ArrayList<>();
        ArrayList<Node> modularList = new ArrayList<>();

        /* 0----
           -----
           --0--
           -----
           ----- */
        pathFinder.board[0][0] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[2][2] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        assertEquals(pathFinder.getValidAdjacentNodes(2, 2, BaseUnit.Team.RED, new HashMap<>()),
          everyAdjacent);

        /* 0----
           -XXX-
           -X0X-
           -XXX-
           ----- */
        pathFinder.board[1][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[1][2] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[2][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[2][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[3][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[3][2] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[3][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        assertEquals(pathFinder.getValidAdjacentNodes(2, 2, BaseUnit.Team.RED, new HashMap<>()),
          emptyList);

        /* 0----
           -XXX-
           -X0--
           -XXX-
           ----- */
        pathFinder.board[2][3] = new BoardCell(null);
        modularList.add(new Node(2, 3));
        assertEquals(pathFinder.getValidAdjacentNodes(2, 2, BaseUnit.Team.RED, new HashMap<>()),
          modularList);

        /* 0----
           --XX-
           -X0--
           -XXX-
           ----- */
        pathFinder.board[1][1] = new BoardCell(null);
        modularList.add(0, new Node(1, 1));
        assertEquals(pathFinder.getValidAdjacentNodes(2, 2, BaseUnit.Team.RED, new HashMap<>()),
          modularList);

        /* 0----
           -000-
           -000-
           -000-
           ----- */
        pathFinder.board[1][1] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[1][2] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[2][1] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[2][3] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[3][1] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[3][2] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[3][3] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        assertEquals(pathFinder.getValidAdjacentNodes(2, 2, BaseUnit.Team.RED, new HashMap<>()),
          everyAdjacent);

        /* 0----
           -000-
           -00--
           -000-
           ----- */
        pathFinder.board[2][3] = new BoardCell(null);
        assertEquals(pathFinder.getValidAdjacentNodes(2, 2, BaseUnit.Team.RED, new HashMap<>()),
          everyAdjacent);

        /* 0----
           --00-
           -00--
           -000-
           ----- */
        pathFinder.board[1][1] = new BoardCell(null);
        assertEquals(pathFinder.getValidAdjacentNodes(2, 2, BaseUnit.Team.RED, new HashMap<>()),
          everyAdjacent);

    }

    @Test
    void testNonEnemyPath() {
        /* 0----
           -----
           --0--
           -----
           ----- */
        pathFinder.board[0][0] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[2][2] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        Path path = new Path();
        path.addNode(1, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 0, BaseUnit.Team.RED, new HashMap<>()),
          path);

        /* 0----
           -XXX-
           -X0X-
           -XXX-
           ----- */
        pathFinder.board[1][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[1][2] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[2][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[2][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[3][1] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[3][2] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        pathFinder.board[3][3] = new BoardCell(new Civilian(BaseUnit.Team.BLUE, 1));
        assertNull(pathFinder.nonEnemyPath(2, 2, 0, 0, Team.RED, new HashMap<>()));

        /* 0----
           -XXX-
           -X0--
           -XXX-
           ----- */
        pathFinder.board[2][3] = new BoardCell(null);
        path = new Path();
        path.addNode(2, 3);
        path.addNode(1, 4);
        path.addNode(0, 3);
        path.addNode(0, 2);
        path.addNode(0, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 0, Team.RED, new HashMap<>()),
          path);

        /* 0----
           --XX-
           -X0--
           -XXX-
           ----- */
        pathFinder.board[1][1] = new BoardCell(null);
        path = new Path();
        path.addNode(1, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 0, Team.RED, new HashMap<>()),
          path);

        /* 0----
           -000-
           -000-
           -000-
           ----- */
        pathFinder.board[1][1] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[1][2] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[2][1] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[2][3] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[3][1] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[3][2] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        pathFinder.board[3][3] = new BoardCell(new Civilian(BaseUnit.Team.RED, 1));
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 0, Team.RED, new HashMap<>()),
          path);

        /* 0----
           -000-
           -00--
           -000-
           ----- */
        pathFinder.board[2][3] = new BoardCell(null);
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 0, Team.RED, new HashMap<>()),
          path);

        /* 0----
           --00-
           -00--
           -000-
           ----- */
        pathFinder.board[1][1] = new BoardCell(null);
        assertEquals(pathFinder.nonEnemyPath(2, 2, 0, 0, Team.RED, new HashMap<>()),
          path);

        /* 0---------
           XXXXXXXXXX
           -00-------
           -000------
           ---------- */
        pathFinder.board[1][0] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][1] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][2] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][4] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][5] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][6] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][7] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][8] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][9] = new BoardCell(new Civilian(Team.BLUE, 1));
        assertNull(pathFinder.nonEnemyPath(2, 2, 0, 0, Team.RED, new HashMap<>()));
    }

    @Test
    void testPathFinder() {
        /* 0---------
           XXXXXXXXXX
           ----------
           ----------
           ---------0 */
        pathFinder.board[0][0] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][0] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][1] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][2] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][4] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][5] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][6] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][7] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][8] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][9] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[4][9] = new BoardCell(new Civilian(Team.RED, 1));
        assertNull(pathFinder.pathFinder(4, 9, 0, 0, Team.RED, new HashMap<>()));

        /* 0---------
           ----------
           ----------
           ----------
           ---------0 */
        pathFinder.board[1][0] = new BoardCell(null);
        pathFinder.board[1][1] = new BoardCell(null);
        pathFinder.board[1][2] = new BoardCell(null);
        pathFinder.board[1][3] = new BoardCell(null);
        pathFinder.board[1][4] = new BoardCell(null);
        pathFinder.board[1][5] = new BoardCell(null);
        pathFinder.board[1][6] = new BoardCell(null);
        pathFinder.board[1][7] = new BoardCell(null);
        pathFinder.board[1][8] = new BoardCell(null);
        pathFinder.board[1][9] = new BoardCell(null);
        Path path = new Path();
        path.addNode(3, 8);
        path.addNode(2, 7);
        path.addNode(1, 6);
        path.addNode(0, 5);
        path.addNode(0, 4);
        path.addNode(0, 3);
        path.addNode(0, 2);
        path.addNode(0, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(4, 9, 0, 0, Team.RED, new HashMap<>()), path);

        /* 0---------
           ----------
           ----------
           ----------
           0--------- */
        pathFinder.board[4][0] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[4][9] = new BoardCell(new Civilian(null, 1));
        path = new Path();
        path.addNode(3, 0);
        path.addNode(2, 0);
        path.addNode(1, 0);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(4, 0, 0, 0, Team.RED, new HashMap<>()), path);

        /* 0--------0
           ----------
           ----------
           ----------
           ---------- */
        pathFinder.board[4][0] = new BoardCell(null);
        pathFinder.board[0][9] = new BoardCell(new Civilian(Team.RED, 1));
        path = new Path();
        path.addNode(0, 8);
        path.addNode(0, 7);
        path.addNode(0, 6);
        path.addNode(0, 5);
        path.addNode(0, 4);
        path.addNode(0, 3);
        path.addNode(0, 2);
        path.addNode(0, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(0, 9, 0, 0, Team.RED, new HashMap<>()), path);

        /* 0XXXXXXXXX
           XXXXXXXXXX
           XXXX0-XXXX
           XXXXXXXXXX
           XXXXXXXXXX */
        populateBoard(new Civilian(Team.BLUE, 1));
        pathFinder.board[0][0] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[2][4] = new BoardCell(new Civilian(Team.RED, 2));
        pathFinder.board[2][5] = new BoardCell(null);
        assertNull(pathFinder.pathFinder(2, 5, 0, 0, Team.RED, new HashMap<>()));

        /* 0---------
           XXXXXXXXX-
           -------0X-
           -XXXXXXXX-
           ---------- */
        populateBoard(null);
        pathFinder.board[0][0] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][0] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][1] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][2] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][4] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][5] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][6] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][7] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][8] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[2][7] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[2][8] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[3][1] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[3][2] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[3][3] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[3][4] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[3][5] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[3][6] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[3][7] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[3][8] = new BoardCell(new Civilian(Team.BLUE, 1));
        path = new Path();
        path.addNode(2, 6);
        path.addNode(2, 5);
        path.addNode(2, 4);
        path.addNode(2, 3);
        path.addNode(2, 2);
        path.addNode(2, 1);
        path.addNode(3, 0);
        path.addNode(4, 1);
        path.addNode(4, 2);
        path.addNode(4, 3);
        path.addNode(4, 4);
        path.addNode(4, 5);
        path.addNode(4, 6);
        path.addNode(4, 7);
        path.addNode(4, 8);
        path.addNode(3, 9);
        path.addNode(2, 9);
        path.addNode(1, 9);
        path.addNode(0, 8);
        path.addNode(0, 7);
        path.addNode(0, 6);
        path.addNode(0, 5);
        path.addNode(0, 4);
        path.addNode(0, 3);
        path.addNode(0, 2);
        path.addNode(0, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(2, 7, 0, 0, Team.RED, new HashMap<>()), path);

        /* 0000000000
           0000000000
           0000000000
           0000000000
           0000000000 */
        populateBoard(new Civilian(Team.RED, 1));
        path = new Path();
        path.addNode(3, 8);
        path.addNode(2, 7);
        path.addNode(1, 6);
        path.addNode(0, 5);
        path.addNode(0, 4);
        path.addNode(0, 3);
        path.addNode(0, 2);
        path.addNode(0, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(4, 9, 0, 0, Team.RED, new HashMap<>()), path);

        /* X---------
           0000000000
           ----------
           ----------
           ---------X */
        pathFinder.board[0][0] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][0] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][1] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][2] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][4] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][5] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][6] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][7] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][8] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][9] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[4][9] = new BoardCell(new Civilian(Team.BLUE, 1));
        assertNull(pathFinder.pathFinder(4, 9, 0, 0, Team.BLUE, new HashMap<>()));

        /* X---------
           ----------
           ----------
           ----------
           ---------X */
        populateBoard(null);
        pathFinder.board[0][0] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[4][9] = new BoardCell(new Civilian(Team.BLUE, 1));
        path = new Path();
        path.addNode(3, 8);
        path.addNode(2, 7);
        path.addNode(1, 6);
        path.addNode(0, 5);
        path.addNode(0, 4);
        path.addNode(0, 3);
        path.addNode(0, 2);
        path.addNode(0, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(4, 9, 0, 0, Team.BLUE, new HashMap<>()), path);

        /* X---------
           ----------
           ----------
           ----------
           X--------- */
        pathFinder.board[4][9] = new BoardCell(null);
        pathFinder.board[4][0] = new BoardCell(new Civilian(Team.BLUE, 1));
        path = new Path();
        path.addNode(3, 0);
        path.addNode(2, 0);
        path.addNode(1, 0);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(4, 0, 0, 0, Team.BLUE, new HashMap<>()), path);

        /* X--------X
           ----------
           ----------
           ----------
           ---------- */
        pathFinder.board[4][0] = new BoardCell(null);
        pathFinder.board[0][9] = new BoardCell(new Civilian(Team.BLUE, 1));
        path = new Path();
        path.addNode(0, 8);
        path.addNode(0, 7);
        path.addNode(0, 6);
        path.addNode(0, 5);
        path.addNode(0, 4);
        path.addNode(0, 3);
        path.addNode(0, 2);
        path.addNode(0, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(0, 9, 0, 0, Team.BLUE, new HashMap<>()), path);

        /* X000000000
           0000000000
           0000X-0000
           0000000000
           0000000000 */
        populateBoard(new Civilian(Team.RED, 1));
        pathFinder.board[0][0] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[2][4] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[2][5] = new BoardCell(null);
        assertNull(pathFinder.pathFinder(2, 5, 0, 0, Team.BLUE, new HashMap<>()));

        /* X---------
           000000000-
           -------X0-
           -00000000-
           ---------- */
        populateBoard(null);
        pathFinder.board[0][0] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[1][0] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][1] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][2] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][3] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][4] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][5] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][6] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][7] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[1][8] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[2][7] = new BoardCell(new Civilian(Team.BLUE, 1));
        pathFinder.board[2][8] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[3][1] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[3][2] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[3][3] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[3][4] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[3][5] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[3][6] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[3][7] = new BoardCell(new Civilian(Team.RED, 1));
        pathFinder.board[3][8] = new BoardCell(new Civilian(Team.RED, 1));
        path = new Path();
        path.addNode(2, 6);
        path.addNode(2, 5);
        path.addNode(2, 4);
        path.addNode(2, 3);
        path.addNode(2, 2);
        path.addNode(2, 1);
        path.addNode(3, 0);
        path.addNode(4, 1);
        path.addNode(4, 2);
        path.addNode(4, 3);
        path.addNode(4, 4);
        path.addNode(4, 5);
        path.addNode(4, 6);
        path.addNode(4, 7);
        path.addNode(4, 8);
        path.addNode(3, 9);
        path.addNode(2, 9);
        path.addNode(1, 9);
        path.addNode(0, 8);
        path.addNode(0, 7);
        path.addNode(0, 6);
        path.addNode(0, 5);
        path.addNode(0, 4);
        path.addNode(0, 3);
        path.addNode(0, 2);
        path.addNode(0, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(2, 7, 0, 0, Team.BLUE, new HashMap<>()), path);

        /* XXXXXXXXXX
           XXXXXXXXXX
           XXXXXXXXXX
           XXXXXXXXXX
           XXXXXXXXXX */
        populateBoard(new Civilian(Team.BLUE, 1));
        path = new Path();
        path.addNode(3, 8);
        path.addNode(2, 7);
        path.addNode(1, 6);
        path.addNode(0, 5);
        path.addNode(0, 4);
        path.addNode(0, 3);
        path.addNode(0, 2);
        path.addNode(0, 1);
        path.addNode(0, 0);
        assertEquals(pathFinder.pathFinder(4, 9, 0, 0, Team.BLUE, new HashMap<>()), path);

        // Out of bounds start
        assertNull(pathFinder.pathFinder(-1, -1, 0, 0, Team.BLUE, new HashMap<>()));
        assertNull(pathFinder.pathFinder(11, 11, 0, 0, Team.BLUE, new HashMap<>()));
        // Out of bounds end
        assertNull(pathFinder.pathFinder(0, 0, -1, -1, Team.BLUE, new HashMap<>()));
        assertNull(pathFinder.pathFinder(0, 0, 11, 11, Team.BLUE, new HashMap<>()));


    }

    void populateBoard(BaseUnit unit) {
        for (int i = 0; i < pathFinder.board.length; i++) {
            for (int j = 0; j < pathFinder.board[0].length; j++) {
                pathFinder.board[i][j] = new BoardCell(unit);
            }
        }
    }
}
