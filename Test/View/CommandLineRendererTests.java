package Test.View;

import static org.junit.jupiter.api.Assertions.*;

import Model.Board;
import Units.Civilian;
import Units.Unit.Team;
import View.CommandLineCell;
import View.CommandLineRenderer;
import com.googlecode.lanterna.TextColor.ANSI;
import org.junit.jupiter.api.Test;

public class CommandLineRendererTests {
    @Test
    void withNewCellAInXY_gettingCellBInSameXY_cellAEqualsCellB() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        CommandLineCell cellA = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellA, 0, 0);
        CommandLineCell cellB = renderer.getCell(0, 0);
        assertEquals(cellA, cellB, "Cell B retrieved does not match Cell A put");
    }
    @Test
    void withNewCommandLineRenderer_gettingWidthEqualsInitialWidth() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        assertEquals(1, renderer.getWidth(), "Current width does not match initial set width");
    }
    @Test
    void withNewCommandLineRenderer_gettingHeightEqualsInitialHeight() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        assertEquals(2, renderer.getHeight(), "Current height does not match initial set height");
    }
    @Test
    void withNewCommandLineRenderer_gettingRefreshRateEqualsInitialRefreshRate() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        assertEquals(30, renderer.getRefreshRate(), "Current refresh rate does not match initial set rate");
    }
    @Test
    void withExistingWidth_settingNewWidth_gettingWidthEqualsNewWidth() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setWidth(3);
        assertEquals(3, renderer.getWidth(), "Current width does not match newly set width");
    }
    @Test
    void withExistingHeight_settingNewHeight_gettingHeightEqualsNewHeight() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setHeight(4);
        assertEquals(4, renderer.getHeight(), "Current height does not match newly set height");
    }
    @Test
    void withExistingRefreshRate_settingNewRefreshRate_gettingRefreshRateEqualsNewRefreshRate() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setRefreshRate(60);
        assertEquals(60, renderer.getRefreshRate(), "Current refresh rate does not match newly set rate");
    }
    @Test
    void withNewCommandLineRenderer_settingNegativeWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(-1, 2, 30);
        assertEquals(1, renderer.getWidth(), "Current width does not equal 1");
    }
    @Test
    void withNewCommandLineRenderer_settingNegativeHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, -2, 30);
        assertEquals(1, renderer.getHeight(), "Current height does not equal 1");
    }
    @Test
    void withNewCommandLineRenderer_settingNegativeRefreshRate_gettingRefreshRateEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, -30);
        assertEquals(1, renderer.getRefreshRate(), "Current refresh rate does not equal 1");
    }
    @Test
    void withExistingWidth_settingNegativeWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setWidth(-3);
        assertEquals(1, renderer.getWidth(), "Current width does not equal 1");
    }
    @Test
    void withExistingHeight_settingNegativeHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setHeight(-4);
        assertEquals(1, renderer.getHeight(), "Current height does not equal 1");
    }
    @Test
    void withExistingRefreshRate_settingNegativeRate_gettingRefreshRateEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setRefreshRate(-60);
        assertEquals(1, renderer.getRefreshRate(), "Current refresh rate does not equal 1");
    }
    @Test
    void withNewCommandLineRenderer_settingZeroWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(0, 2, 30);
        assertEquals(1, renderer.getWidth(), "Current width does not equal 1");
    }
    @Test
    void withNewCommandLineRenderer_settingZeroHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 0, 30);
        assertEquals(1, renderer.getHeight(), "Current board height does not equal 1");
    }
    @Test
    void withNewCommandLineRenderer_settingZeroRefreshRate_gettingRefreshRateEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 0);
        assertEquals(1, renderer.getRefreshRate(), "Current refresh rate does not equal 1");
    }
    @Test
    void withExistingWidth_settingZeroWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setWidth(0);
        assertEquals(1, renderer.getWidth(), "Current width does not equal 1");
    }
    @Test
    void withExistingHeight_settingZeroHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setHeight(0);
        assertEquals(1, renderer.getHeight(), "Current height does not equal 1");
    }
    @Test
    void withExistingRefreshRate_settingZeroRate_gettingRefreshRateEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setRefreshRate(0);
        assertEquals(1, renderer.getRefreshRate(), "Current refresh rate does not equal 1");
    }
    @Test
    void withExistingCellInXY_puttingNullBoardInSameXY_gettingCellInSameXYEqualsExistingCell() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        CommandLineCell cell = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cell, 0, 0);
        renderer.putBoard(null, 0, 0);
        assertEquals(cell, renderer.getCell(0, 0), "Cell B retrieved does not match Cell A initially put");
    }
    @Test
    void withExistingCellInXY_puttingBoardInSameXY_gettingCellInSameXYEqualsFirstCellInBoard() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 1, 30);
        Board board = new Board(1, 1);
        Civilian civilian = new Civilian(Team.RED, 0);
        board.board[0][0].addUnit(civilian);
        renderer.putBoard(board, 0, 0);
        assertEquals(civilian.toCommandLineCell(), renderer.getCell(0, 0), "Red Civilian not found in first cell (0, 0)");
    }
    @Test
    void withBoardInXY_gettingCellFromXYPlusBoardSize_cellEqualsLastCellInBoard() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2, 30);
        Board board = new Board(2, 2);
        Civilian civilian = new Civilian(Team.RED, 0);
        board.board[1][1].addUnit(civilian);
        renderer.putBoard(board, 0, 0);
        assertEquals(civilian.toCommandLineCell(), renderer.getCell(1, 1), "Red Civilian not found in last cell (1, 1)");
    }
    @Test
    void withCellAInFirstColumn_puttingCellBInNegativeX_cellInFirstColumnEqualsB() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2, 30);
        CommandLineCell cellB = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellB, -1, 0);
        assertEquals(cellB, renderer.getCell(0, 0), "Cell B not found in first column (0, 0)");
        assertNotEquals(cellB, renderer.getCell(1, 0), "Cell B found in (1, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 1), "Cell B found in (0, 1)");
        assertNotEquals(cellB, renderer.getCell(1, 1), "Cell B found in (1, 1)");
    }
    @Test
    void withCellAInLastColumn_puttingCellBInXGreaterThanRendererWidth_cellInLastColumnEqualsB() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2, 30);
        CommandLineCell cellB = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellB, 3, 0);
        assertEquals(cellB, renderer.getCell(1, 0), "cellB not found in last column (1, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 0), "cellB found in (0, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 1), "cellB found in (0, 1)");
        assertNotEquals(cellB, renderer.getCell(1, 1), "cellB found in (1, 1)");
    }
    @Test
    void withCellAInFirstRow_puttingCellBInNegativeY_cellInFirstRowEqualsB() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2, 30);
        CommandLineCell cellB = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellB, 0, -1);
        assertEquals(cellB, renderer.getCell(0, 0), "cell B not found in first row (0, 0)");
        assertNotEquals(cellB, renderer.getCell(1, 0), "cell B found in (1, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 1), "cell B found in (0, 1)");
        assertNotEquals(cellB, renderer.getCell(1, 1), "cell B found in (1, 1)");
    }
    @Test
    void withCellAInLastRow_puttingCellBInYGreaterThanRendererHeight_cellInLastRowEqualsB() {
        CommandLineRenderer renderer = new CommandLineRenderer(2, 2, 30);
        CommandLineCell cellB = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cellB, 0, 3);
        assertEquals(cellB, renderer.getCell(0, 1), "Cell B not found in last row (0, 1)");
        assertNotEquals(cellB, renderer.getCell(1, 0), "Cell B found in (1, 0)");
        assertNotEquals(cellB, renderer.getCell(0, 0), "Cell B found in (0, 0)");
        assertNotEquals(cellB, renderer.getCell(1, 1), "Cell B found in (1, 1)");
    }
}
