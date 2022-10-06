package com.brosintime.rts.View;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.googlecode.lanterna.TextColor.ANSI;
import org.junit.jupiter.api.Test;

public class TerminalCellTests {

    @Test
    void withCellA_creatingIdenticalCellB_cellAEqualsB() {
        TerminalCell terminalCellA = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        TerminalCell terminalCellB = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        assertEquals(terminalCellA, terminalCellB);
        assertEquals(terminalCellA, terminalCellB);
    }

    @Test
    void withCellA_creatingIdenticalCellB_cellBEqualsA() {
        TerminalCell terminalCellA = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        TerminalCell terminalCellB = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        assertEquals(terminalCellB, terminalCellA);
        assertEquals(terminalCellB, terminalCellA);
    }

    @Test
    void withCellA_creatingDifferentCellB_cellADoesNotEqualsB() {
        TerminalCell terminalCellA = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        TerminalCell terminalCellB = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'B');
        assertNotEquals(terminalCellA, terminalCellB);
        assertNotEquals(terminalCellA, terminalCellB);
        terminalCellB = new TerminalCell(ANSI.YELLOW, ANSI.DEFAULT, 'A');
        assertNotEquals(terminalCellA, terminalCellB);
        assertNotEquals(terminalCellA, terminalCellB);
        terminalCellB = new TerminalCell(ANSI.DEFAULT, ANSI.YELLOW, 'A');
        assertNotEquals(terminalCellA, terminalCellB);
        assertNotEquals(terminalCellA, terminalCellB);
    }

    @Test
    void withCellA_creatingDifferentCellB_cellBDoesNotEqualsA() {
        TerminalCell terminalCellA = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        TerminalCell terminalCellB = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'B');
        assertNotEquals(terminalCellB, terminalCellA);
        assertNotEquals(terminalCellB, terminalCellA);
        terminalCellB = new TerminalCell(ANSI.YELLOW, ANSI.DEFAULT, 'B');
        assertNotEquals(terminalCellB, terminalCellA);
        assertNotEquals(terminalCellB, terminalCellA);
        terminalCellB = new TerminalCell(ANSI.DEFAULT, ANSI.YELLOW, 'B');
        assertNotEquals(terminalCellB, terminalCellA);
        assertNotEquals(terminalCellB, terminalCellA);
    }

    @Test
    void withCellA_creatingNullCellB_cellADoesNotEqualB() {
        TerminalCell terminalCellA = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        TerminalCell terminalCellB = null;
        assertNotEquals(terminalCellA, terminalCellB);
        assertNotEquals(terminalCellA, terminalCellB);
    }

    @Test
    void withCellA_cellAEqualsItself() {
        TerminalCell terminalCellA = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        assertEquals(terminalCellA, terminalCellA);
        assertEquals(terminalCellA, terminalCellA);
    }

    @Test
    void withBlankCellA_creatingNewCellBWithDefaultColorsAndSpaceCharacter_cellAEqualsB() {
        TerminalCell terminalCellA = Cell.blank();
        TerminalCell terminalCellB = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, ' ');
        assertEquals(terminalCellA, terminalCellB);
        assertEquals(terminalCellA, terminalCellB);
    }

    @Test
    void withBlankCellA_creatingNewCellBWithDefaultColorsAndSpaceCharacter_cellBEqualsA() {
        TerminalCell terminalCellA = Cell.blank();
        TerminalCell terminalCellB = new TerminalCell(ANSI.DEFAULT, ANSI.DEFAULT, ' ');
        assertEquals(terminalCellB, terminalCellA);
        assertEquals(terminalCellB, terminalCellA);
    }
}
