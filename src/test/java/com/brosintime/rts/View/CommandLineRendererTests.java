package com.brosintime.rts.View;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.brosintime.rts.Model.Board;
import com.brosintime.rts.Model.Node;
import com.brosintime.rts.Units.Civilian;
import com.brosintime.rts.Units.Unit.Team;
import com.googlecode.lanterna.TextColor.ANSI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommandLineRendererTests {

    @Test
    @DisplayName("The cell put at a point should equal the cell retrieved afterward from the same point.")
    void withNewCellAInXY_gettingCellBInSameXY_cellAEqualsCellB() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2);
        Cell cellA = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellA, 0, 0);
        renderer.flush();
        Cell cellB = renderer.getCell(0, 0);
        assertEquals(cellA, cellB, "Cell B retrieved does not match Cell A put");
    }

    @Test
    @DisplayName("Retrieving the renderer width should match the width we created it with.")
    void withNewCommandLineRenderer_gettingWidthEqualsInitialWidth() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2);
        assertEquals(1, renderer.width(), "Current width does not match initial set width");
    }

    @Test
    @DisplayName("Retrieving the renderer height should match the height we created it with.")
    void withNewCommandLineRenderer_gettingHeightEqualsInitialHeight() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2);
        assertEquals(2, renderer.height(), "Current height does not match initial set height");
    }

    @Test
    @DisplayName("The renderer width should equal one after creating it with negative width.")
    void withNewCommandLineRenderer_settingNegativeWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(-1, 2);
        assertEquals(1, renderer.width(), "Current width does not equal 1");
    }

    @Test
    @DisplayName("The renderer height should equal one after creating it with negative height.")
    void withNewCommandLineRenderer_settingNegativeHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, -2);
        assertEquals(1, renderer.height(), "Current height does not equal 1");
    }

    @Test
    @DisplayName("The renderer width should equal one after creating it with zero width.")
    void withNewCommandLineRenderer_settingZeroWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(0, 2);
        assertEquals(1, renderer.width(), "Current width does not equal 1");
    }

    @Test
    @DisplayName("The renderer height should equal one after creating it with zero height.")
    void withNewCommandLineRenderer_settingZeroHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 0);
        assertEquals(1, renderer.height(), "Current board height does not equal 1");
    }

    @Test
    @DisplayName("After putting a board to (0, 0), the first screen cell should match the first board cell.")
    void withExistingCellInXY_puttingBoardInSameXY_gettingCellInSameXYEqualsFirstCellInBoard() {
        /*
        ...   B     C..
        ... +     = ... C = B
        ...         ...
         */
        CommandLineRenderer renderer = new CommandLineRenderer(1, 1);
        Board board = new Board(1, 1);
        Civilian civilian = new Civilian(Team.RED, 0);
        board.board[0][0].addUnit(civilian);
        renderer.putBoard(board, 0, 0);
        renderer.flush();
        assertEquals(civilian.toCell(), renderer.getCell(0, 0),
            "Red Civilian not found in first cell (0, 0)");
    }

    @Test
    @DisplayName("After putting a board to the screen, retrieving the screen cell from the position the last board cell is at should match the last board cell.")
    void withBoardInXY_gettingCellFromXYPlusBoardSize_cellEqualsLastCellInBoard() {
        /*
        ...   B     ...
        ... + 3x3 = ... C = X
        ...     X   ..C
         */
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2);
        Board board = new Board(2, 2);
        Civilian civilian = new Civilian(Team.RED, 0);
        board.board[1][1].addUnit(civilian);
        renderer.putBoard(board, 0, 0);
        renderer.flush();
        assertEquals(civilian.toCell(), renderer.getCell(1, 1),
            "Red Civilian not found in last cell (1, 1)");
    }

    @Test
    @DisplayName("Putting a cell out of bounds in negative x direction should replace the screen cell at the first column.")
    void withCellAInFirstColumn_puttingCellBInNegativeX_cellInFirstColumnEqualsB() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2);
        Cell cellB = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellB, -1, 0);
        renderer.flush();
        assertEquals(cellB, renderer.getCell(0, 0), "Cell B not found in first column (0, 0)");
        assertNotEquals(cellB, renderer.getCell(1, 0), "Cell B found in (1, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 1), "Cell B found in (0, 1)");
        assertNotEquals(cellB, renderer.getCell(1, 1), "Cell B found in (1, 1)");
    }

    @Test
    @DisplayName("Putting a cell out of bounds in positive x direction should replace the screen cell at the last column.")
    void withCellAInLastColumn_puttingCellBInXGreaterThanRendererWidth_cellInLastColumnEqualsB() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2);
        Cell cellB = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellB, 3, 0);
        renderer.flush();
        assertEquals(cellB, renderer.getCell(1, 0), "cellB not found in last column (1, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 0), "cellB found in (0, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 1), "cellB found in (0, 1)");
        assertNotEquals(cellB, renderer.getCell(1, 1), "cellB found in (1, 1)");
    }

    @Test
    @DisplayName("Putting a cell out of bounds in negative y direction should replace the screen cell at the first row.")
    void withCellAInFirstRow_puttingCellBInNegativeY_cellInFirstRowEqualsB() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2);
        Cell cellB = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellB, 0, -1);
        renderer.flush();
        assertEquals(cellB, renderer.getCell(0, 0), "cell B not found in first row (0, 0)");
        assertNotEquals(cellB, renderer.getCell(1, 0), "cell B found in (1, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 1), "cell B found in (0, 1)");
        assertNotEquals(cellB, renderer.getCell(1, 1), "cell B found in (1, 1)");
    }

    @Test
    @DisplayName("Putting a cell out of bounds in positive y direction should replace the screen cell at the last row.")
    void withCellAInLastRow_puttingCellBInYGreaterThanRendererHeight_cellInLastRowEqualsB() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2);
        Cell cellB = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellB, 0, 3);
        renderer.flush();
        assertEquals(cellB, renderer.getCell(0, 1), "Cell B not found in last row (0, 1)");
        assertNotEquals(cellB, renderer.getCell(1, 0), "Cell B found in (1, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 0), "Cell B found in (0, 0)");
        assertNotEquals(cellB, renderer.getCell(1, 1), "Cell B found in (1, 1)");
    }

    @Test
    @DisplayName("Drawing an outline should not affect cells outside the outline bounds.")
    void withExistingCells_drawingAnOutline_cellsOutsideEqualExistingCells() {
        /*
        EEEEE               EEEEE
        E   E      OOO      EOOOE
        E   E  +   O O   =  EO OE
        E   E      OOO      EOOOE
        EEEEE               EEEEE
         */
        CommandLineRenderer renderer = new CommandLineRenderer(5, 5);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Node from = new Node(1, 1);
        Node to = new Node(4, 4);
        renderer.drawRectangleOutline(outline, from, to);
        renderer.flush();
        assertNotEquals(outline, renderer.getCell(0, 0), "Outline cell found at (0, 0)");
        assertNotEquals(outline, renderer.getCell(2, 0), "Outline cell found at (2, 0)");
        assertNotEquals(outline, renderer.getCell(4, 0), "Outline cell found at (4, 0)");
        assertNotEquals(outline, renderer.getCell(4, 2), "Outline cell found at (4, 2)");
        assertNotEquals(outline, renderer.getCell(4, 4), "Outline cell found at (4, 4)");
        assertNotEquals(outline, renderer.getCell(2, 4), "Outline cell found at (2, 4)");
        assertNotEquals(outline, renderer.getCell(0, 4), "Outline cell found at (0, 4)");
        assertNotEquals(outline, renderer.getCell(0, 2), "Outline cell found at (0, 2)");
    }

    @Test
    @DisplayName("Drawing an outline should not affect cells inside the outline bounds.")
    void withExistingCells_drawingAnOutline_cellsInsideEqualExistingCells() {
        /*

                   OOO       OOO
          E    +   O O   =   OEO
                   OOO       OOO

         */
        CommandLineRenderer renderer = new CommandLineRenderer(5, 5);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Node from = new Node(1, 1);
        Node to = new Node(4, 4);
        renderer.drawRectangleOutline(outline, from, to);
        renderer.flush();
        assertNotEquals(outline, renderer.getCell(2, 2), "Outline cell found at (2, 2)");
    }

    @Test
    @DisplayName("Drawing an outline should replace all cells under the outline.")
    void withExistingCells_drawingAnOutline_cellsUnderOutlineEqualOutlineCell() {
        /*

         EEE       OOO       OOO
         E E   +   O O   =   O O
         EEE       OOO       OOO

         */
        CommandLineRenderer renderer = new CommandLineRenderer(5, 5);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Node from = new Node(1, 1);
        Node to = new Node(4, 4);
        renderer.drawRectangleOutline(outline, from, to);
        renderer.flush();
        assertEquals(outline, renderer.getCell(1, 1), "Outline cell not found at (1, 1)");
        assertEquals(outline, renderer.getCell(2, 1), "Outline cell not found at (2, 1)");
        assertEquals(outline, renderer.getCell(3, 1), "Outline cell not found at (3, 1)");
        assertEquals(outline, renderer.getCell(3, 2), "Outline cell not found at (3, 2)");
        assertEquals(outline, renderer.getCell(3, 3), "Outline cell not found at (3, 3)");
        assertEquals(outline, renderer.getCell(2, 3), "Outline cell not found at (2, 3)");
        assertEquals(outline, renderer.getCell(1, 3), "Outline cell not found at (1, 3)");
        assertEquals(outline, renderer.getCell(1, 2), "Outline cell not found at (1, 2)");
    }

    @Test
    @DisplayName("Drawing a zero-or-less-sized outline should not affect any cells.")
    void withExistingCells_drawingAZeroOrLessSizedOutline_cellsEqualExistingCells() {
        /*
        EEEEE               EEEEE
        EEEEE               EEEEE
        EEEEE  +    0    =  EEEEE
        EEEEE               EEEEE
        EEEEE               EEEEE
         */
        CommandLineRenderer renderer = new CommandLineRenderer(1, 1);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Node from = new Node(0, 0);
        Node to = new Node(0, 0);
        renderer.drawRectangleOutline(outline, from, to);
        renderer.flush();
        assertNotEquals(outline, renderer.getCell(0, 0), "Outline cell found at (0, 0)");
    }

    @Test
    @DisplayName("Drawing an outline larger than the screen gets cut off at the out-of-bounds dimension.")
    void withExistingCells_drawingAnOutlineLargerThanScreen_cellsAtLastRowOrColumnEqualExistingCells() {
        /*
                offset +1
        .....     OOOOO     .....
        .....     O   O     .OOOO
        .....  +  O   O  =  .O...
        .....     O   O     .O...
        .....     OOOOO     .O...
         */
        CommandLineRenderer renderer = new CommandLineRenderer(3, 3);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Node from = new Node(1, 1);
        Node to = new Node(4, 4);
        renderer.drawRectangleOutline(outline, from, to);
        renderer.flush();
        assertEquals(outline, renderer.getCell(2, 1), "Outline cell found at (2, 1)");
        assertNotEquals(outline, renderer.getCell(2, 2), "Outline cell not found at (2, 2)");
        assertEquals(outline, renderer.getCell(1, 2), "Outline cell found at (1, 2)");
    }

    @Test
    @DisplayName("Drawing a box should not affect cells outside the box bounds")
    void withExistingCells_drawingABox_cellsOutsideEqualExistingCells() {
        /*
        .....               .....
        .   .      OOO      .OOO.
        .   .  +   OFO   =  .OFO.
        .   .      OOO      .OOO.
        .....               .....
         */
        CommandLineRenderer renderer = new CommandLineRenderer(5, 5);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Cell fill = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'F');
        Cell blank = Cell.blank();
        Node from = new Node(1, 1);
        Node to = new Node(4, 4);
        renderer.drawRectangle(outline, fill, from, to);
        renderer.flush();
        assertEquals(blank, renderer.getCell(0, 0), "Existing blank cell not found at (0, 0)");
        assertEquals(blank, renderer.getCell(2, 0), "Existing blank cell not found at (2, 0)");
        assertEquals(blank, renderer.getCell(4, 0), "Existing blank cell not found at (4, 0)");
        assertEquals(blank, renderer.getCell(4, 2), "Existing blank cell not found at (4, 2)");
        assertEquals(blank, renderer.getCell(4, 4), "Existing blank cell not found at (4, 4)");
        assertEquals(blank, renderer.getCell(2, 4), "Existing blank cell not found at (2, 4)");
        assertEquals(blank, renderer.getCell(0, 4), "Existing blank cell not found at (0, 4)");
        assertEquals(blank, renderer.getCell(0, 2), "Existing blank cell not found at (0, 2)");
    }

    @Test
    @DisplayName("Drawing a box should replace all cells under the outline with the outline cell.")
    void withExistingCells_drawingABox_cellsUnderOutlineEqualOutlineCell() {
        /*
        EEEEE     OOOOO     OOOOO
        E   E     O...O     O...O
        E   E  +  O...O  =  O...O
        E   E     O...O     O...O
        EEEEE     OOOOO     OOOOO
         */
        CommandLineRenderer renderer = new CommandLineRenderer(5, 5);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Cell fill = new CommandLineCell(ANSI.RED, ANSI.BLACK, '.');
        Node from = new Node(1, 1);
        Node to = new Node(4, 4);
        renderer.drawRectangle(outline, fill, from, to);
        renderer.flush();
        assertEquals(outline, renderer.getCell(1, 1), "Outline cell not found at (1, 1)");
        assertEquals(outline, renderer.getCell(2, 1), "Outline cell not found at (2, 1)");
        assertEquals(outline, renderer.getCell(3, 1), "Outline cell not found at (3, 1)");
        assertEquals(outline, renderer.getCell(3, 2), "Outline cell not found at (3, 2)");
        assertEquals(outline, renderer.getCell(3, 3), "Outline cell not found at (3, 3)");
        assertEquals(outline, renderer.getCell(2, 3), "Outline cell not found at (2, 3)");
        assertEquals(outline, renderer.getCell(1, 3), "Outline cell not found at (1, 3)");
        assertEquals(outline, renderer.getCell(1, 2), "Outline cell not found at (1, 2)");
    }

    @Test
    @DisplayName("Drawing a box should replace all cells under the fill with the fill cell.")
    void withExistingCells_drawingABox_cellsUnderFillEqualFillCell() {
        /*
                  OOOOO     OOOOO
         EEE      O...O     O...O
         EEE   +  O...O  =  O...O
         EEE      O...O     O...O
                  OOOOO     OOOOO
         */
        CommandLineRenderer renderer = new CommandLineRenderer(5, 5);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Cell fill = new CommandLineCell(ANSI.RED, ANSI.BLACK, '.');
        Node from = new Node(1, 1);
        Node to = new Node(4, 4);
        renderer.drawRectangle(outline, fill, from, to);
        renderer.flush();
        assertEquals(fill, renderer.getCell(2, 2), "Fill cell not found at (2, 2)");
    }

    @Test
    @DisplayName("Drawing a zero-or-less-sized box should not affect any cells.")
    void withExistingCells_drawingAZeroOrLessSizedBox_cellsEqualExistingCells() {
        /*
        EEEEE               EEEEE
        EEEEE               EEEEE
        EEEEE  +    0    =  EEEEE
        EEEEE               EEEEE
        EEEEE               EEEEE
         */
        CommandLineRenderer renderer = new CommandLineRenderer(1, 1);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Cell fill = new CommandLineCell(ANSI.RED, ANSI.BLACK, '.');
        Node from = new Node(0, 0);
        Node to = new Node(0, 0);
        renderer.drawRectangle(outline, fill, from, to);
        renderer.flush();
        assertNotEquals(outline, renderer.getCell(0, 0), "Outline cell found at (0, 0)");
        assertNotEquals(fill, renderer.getCell(0, 0), "Fill cell found at (0, 0)");
    }

    @Test
    @DisplayName("Drawing a one-or-two-sized box should not put any fill cells to the screen.")
    void withExistingCells_drawingAOneOrTwoSizedBox_cellsUnderBoxAllEqualOutlineCell() {
        /*
        .....               .....
        .....               .OO..
        .....  +  size:2 =  .OO..
        .....               .....
        .....               .....
         */
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Cell fill = new CommandLineCell(ANSI.RED, ANSI.BLACK, '.');
        Node from = new Node(0, 0);
        Node to = new Node(2, 2);
        renderer.drawRectangle(outline, fill, from, to);
        renderer.flush();
        assertEquals(outline, renderer.getCell(0, 0), "Outline cell not found at (0, 0)");
        assertEquals(outline, renderer.getCell(1, 0), "Outline cell not found at (1, 0)");
        assertEquals(outline, renderer.getCell(0, 1), "Outline cell not found at (0, 1)");
        assertEquals(outline, renderer.getCell(1, 1), "Outline cell not found at (1, 1)");
    }

    @Test
    @DisplayName("Drawing a box larger than the screen gets cut off at the out-of-bounds dimension, with the last row/column matching the fill cell.")
    void withExistingCells_drawingABoxLargerThanScreen_cellsAtLastRowOrColumnEqualFillCell() {
        /*
                offset +1
        .....     OOOOO     .....
        .....     O'''O     .OOOO
        .....  +  O'''O  =  .O'''
        .....     O'''O     .O'''
        .....     OOOOO     .O'''
         */
        CommandLineRenderer renderer = new CommandLineRenderer(3, 3);
        Cell outline = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'O');
        Cell fill = new CommandLineCell(ANSI.RED, ANSI.BLACK, '.');
        Node from = new Node(1, 1);
        Node to = new Node(4, 4);
        renderer.drawRectangle(outline, fill, from, to);
        renderer.flush();
        assertEquals(outline, renderer.getCell(2, 1), "Outline cell not found at (2, 1)");
        assertEquals(fill, renderer.getCell(2, 2), "Fill cell not found at (2, 2)");
        assertEquals(outline, renderer.getCell(1, 2), "Outline cell not found at (1, 2)");
    }

    @Test
    @DisplayName("Drawing a string should put the first character in the starting location on the screen.")
    void withExistingCells_puttingAString_cellInStartingLocationMatchesFirstCharacterInString() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 1);
        renderer.putString("H", ANSI.RED, ANSI.BLACK, 0, 0);
        renderer.flush();
        assertEquals('H', renderer.getCell(0, 0).character(),
            "First character 'H' in string not found in (0, 0)");
    }

    @Test
    @DisplayName("After putting a string to the screen, retrieving the screen cell from the position the last character in the string is at should match the last board cell.")
    void withStringPut_gettingCellFromStartingLocationPlusStringLength_cellMatchesLastCharacterInString() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 1);
        renderer.putString("Hi", ANSI.RED, ANSI.BLACK, 0, 0);
        renderer.flush();
        assertEquals('i', renderer.getCell(1, 0).character(),
            "Last character 'i' in string not found in (1, 0)");
    }

    @Test
    @DisplayName("Drawing a string that extends out-of-bounds of screen should be cropped at the screen edge.")
    void withStringExtendingPastScreenBounds_gettingLastCellInColumn_cellDoesNotMatchLastCharacterInString() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 1);
        renderer.putString("Hello", ANSI.RED, ANSI.BLACK, 0, 0);
        renderer.flush();
        assertEquals('e', renderer.getCell(1, 0).character(),
            "Second character 'e' in string not found in (1, 0)");
    }

    @Test
    @DisplayName("Drawing a string at out-of-bounds starting position should put the first character within screen bounds.")
    void withStringPutOutOfBounds_gettingCellAtEdgeOfScreen_cellMatchesFirstCharacterInString() {
        CommandLineRenderer renderer = new CommandLineRenderer(3, 3);
        /*
        X..
        ...
        ...
         */
        renderer.putString("123456789", ANSI.RED, ANSI.BLACK, -1, -1);
        renderer.flush();
        assertEquals('1', renderer.getCell(0, 0).character(),
            "First character '1' in string not found in (0, 0)");
        renderer.clear();
        /*
        .X.
        ...
        ...
         */
        renderer.putString("123456789", ANSI.RED, ANSI.BLACK, 1, -1);
        renderer.flush();
        assertEquals('1', renderer.getCell(1, 0).character(),
            "First character '1' in string not found in (1, 0)");
        renderer.clear();
        /*
        ..X
        ...
        ...
         */
        renderer.putString("123456789", ANSI.RED, ANSI.BLACK, 3, -1);
        renderer.flush();
        assertEquals('1', renderer.getCell(2, 0).character(),
            "First character '1' in string not found in (2, 0)");
        renderer.clear();
        /*
        ...
        ..X
        ...
         */
        renderer.putString("123456789", ANSI.RED, ANSI.BLACK, 3, 1);
        renderer.flush();
        assertEquals('1', renderer.getCell(2, 1).character(),
            "First character '1' in string not found in (2, 1)");
        renderer.clear();
        /*
        ...
        ...
        ..X
         */
        renderer.putString("123456789", ANSI.RED, ANSI.BLACK, 3, 3);
        renderer.flush();
        assertEquals('1', renderer.getCell(2, 2).character(),
            "First character '1' in string not found in (2, 2)");
        renderer.clear();
        /*
        ...
        ...
        .X.
         */
        renderer.putString("123456789", ANSI.RED, ANSI.BLACK, 1, 3);
        renderer.flush();
        assertEquals('1', renderer.getCell(1, 2).character(),
            "First character '1' in string not found in (1, 2)");
        renderer.clear();
        /*
        ...
        ...
        X..
         */
        renderer.putString("123456789", ANSI.RED, ANSI.BLACK, -1, 3);
        renderer.flush();
        assertEquals('1', renderer.getCell(0, 2).character(),
            "First character '1' in string not found in (0, 2)");
        renderer.clear();
        /*
        ...
        X..
        ...
         */
        renderer.putString("123456789", ANSI.RED, ANSI.BLACK, -1, 1);
        renderer.flush();
        assertEquals('1', renderer.getCell(0, 1).character(),
            "First character '1' in string not found in (0, 1)");
    }
}
