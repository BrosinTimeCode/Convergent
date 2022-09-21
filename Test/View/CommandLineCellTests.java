package Test.View;

import static org.junit.jupiter.api.Assertions.*;

import View.CommandLineCell;
import com.googlecode.lanterna.TextColor.ANSI;
import org.junit.jupiter.api.Test;

public class CommandLineCellTests {

    @Test
    void withCellA_creatingIdenticalCellB_cellAEqualsB() {
        CommandLineCell cellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell cellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        assertEquals(cellA, cellB);
        assertEquals(cellA, cellB);
    }

    @Test
    void withCellA_creatingIdenticalCellB_cellBEqualsA() {
        CommandLineCell cellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell cellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        assertEquals(cellB, cellA);
        assertEquals(cellB, cellA);
    }

    @Test
    void withCellA_creatingDifferentCellB_cellADoesNotEqualsB() {
        CommandLineCell cellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell cellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'B');
        assertNotEquals(cellA, cellB);
        assertNotEquals(cellA, cellB);
        cellB = new CommandLineCell(ANSI.YELLOW, ANSI.DEFAULT, 'A');
        assertNotEquals(cellA, cellB);
        assertNotEquals(cellA, cellB);
        cellB = new CommandLineCell(ANSI.DEFAULT, ANSI.YELLOW, 'A');
        assertNotEquals(cellA, cellB);
        assertNotEquals(cellA, cellB);
    }

    @Test
    void withCellA_creatingDifferentCellB_cellBDoesNotEqualsA() {
        CommandLineCell cellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell cellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'B');
        assertNotEquals(cellB, cellA);
        assertNotEquals(cellB, cellA);
        cellB = new CommandLineCell(ANSI.YELLOW, ANSI.DEFAULT, 'B');
        assertNotEquals(cellB, cellA);
        assertNotEquals(cellB, cellA);
        cellB = new CommandLineCell(ANSI.DEFAULT, ANSI.YELLOW, 'B');
        assertNotEquals(cellB, cellA);
        assertNotEquals(cellB, cellA);
    }

    @Test
    void withCellA_creatingNullCellB_cellADoesNotEqualB() {
        CommandLineCell cellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell cellB = null;
        assertNotEquals(cellA, cellB);
        assertNotEquals(cellA, cellB);
    }

    @Test
    void withCellA_cellAEqualsItself() {
        CommandLineCell cellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        assertEquals(cellA, cellA);
        assertEquals(cellA, cellA);
    }

    @Test
    void withBlankCellA_creatingNewCellBWithDefaultColorsAndSpaceCharacter_cellAEqualsB() {
        CommandLineCell cellA = CommandLineCell.newBlank();
        CommandLineCell cellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, ' ');
        assertEquals(cellA, cellB);
        assertEquals(cellA, cellB);
    }

    @Test
    void withBlankCellA_creatingNewCellBWithDefaultColorsAndSpaceCharacter_cellBEqualsA() {
        CommandLineCell cellA = CommandLineCell.newBlank();
        CommandLineCell cellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, ' ');
        assertEquals(cellB, cellA);
        assertEquals(cellB, cellA);
    }
}
