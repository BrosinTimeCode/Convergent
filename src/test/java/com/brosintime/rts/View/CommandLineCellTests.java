package com.brosintime.rts.View;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.googlecode.lanterna.TextColor.ANSI;
import org.junit.jupiter.api.Test;

public class CommandLineCellTests {

    @Test
    void withCellA_creatingIdenticalCellB_cellAEqualsB() {
        CommandLineCell commandLineCellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell commandLineCellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        assertEquals(commandLineCellA, commandLineCellB);
        assertEquals(commandLineCellA, commandLineCellB);
    }

    @Test
    void withCellA_creatingIdenticalCellB_cellBEqualsA() {
        CommandLineCell commandLineCellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell commandLineCellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        assertEquals(commandLineCellB, commandLineCellA);
        assertEquals(commandLineCellB, commandLineCellA);
    }

    @Test
    void withCellA_creatingDifferentCellB_cellADoesNotEqualsB() {
        CommandLineCell commandLineCellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell commandLineCellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'B');
        assertNotEquals(commandLineCellA, commandLineCellB);
        assertNotEquals(commandLineCellA, commandLineCellB);
        commandLineCellB = new CommandLineCell(ANSI.YELLOW, ANSI.DEFAULT, 'A');
        assertNotEquals(commandLineCellA, commandLineCellB);
        assertNotEquals(commandLineCellA, commandLineCellB);
        commandLineCellB = new CommandLineCell(ANSI.DEFAULT, ANSI.YELLOW, 'A');
        assertNotEquals(commandLineCellA, commandLineCellB);
        assertNotEquals(commandLineCellA, commandLineCellB);
    }

    @Test
    void withCellA_creatingDifferentCellB_cellBDoesNotEqualsA() {
        CommandLineCell commandLineCellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell commandLineCellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'B');
        assertNotEquals(commandLineCellB, commandLineCellA);
        assertNotEquals(commandLineCellB, commandLineCellA);
        commandLineCellB = new CommandLineCell(ANSI.YELLOW, ANSI.DEFAULT, 'B');
        assertNotEquals(commandLineCellB, commandLineCellA);
        assertNotEquals(commandLineCellB, commandLineCellA);
        commandLineCellB = new CommandLineCell(ANSI.DEFAULT, ANSI.YELLOW, 'B');
        assertNotEquals(commandLineCellB, commandLineCellA);
        assertNotEquals(commandLineCellB, commandLineCellA);
    }

    @Test
    void withCellA_creatingNullCellB_cellADoesNotEqualB() {
        CommandLineCell commandLineCellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        CommandLineCell commandLineCellB = null;
        assertNotEquals(commandLineCellA, commandLineCellB);
        assertNotEquals(commandLineCellA, commandLineCellB);
    }

    @Test
    void withCellA_cellAEqualsItself() {
        CommandLineCell commandLineCellA = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, 'A');
        assertEquals(commandLineCellA, commandLineCellA);
        assertEquals(commandLineCellA, commandLineCellA);
    }

    @Test
    void withBlankCellA_creatingNewCellBWithDefaultColorsAndSpaceCharacter_cellAEqualsB() {
        CommandLineCell commandLineCellA = Cell.blank();
        CommandLineCell commandLineCellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, ' ');
        assertEquals(commandLineCellA, commandLineCellB);
        assertEquals(commandLineCellA, commandLineCellB);
    }

    @Test
    void withBlankCellA_creatingNewCellBWithDefaultColorsAndSpaceCharacter_cellBEqualsA() {
        CommandLineCell commandLineCellA = Cell.blank();
        CommandLineCell commandLineCellB = new CommandLineCell(ANSI.DEFAULT, ANSI.DEFAULT, ' ');
        assertEquals(commandLineCellB, commandLineCellA);
        assertEquals(commandLineCellB, commandLineCellA);
    }
}
