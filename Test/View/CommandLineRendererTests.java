package Test.View;

import static org.junit.jupiter.api.Assertions.*;

import Model.Board;
import Units.BaseUnit.Team;
import Units.Civilian;
import View.CommandLineCell;
import View.CommandLineRenderer;
import com.googlecode.lanterna.TextColor.ANSI;
import org.junit.jupiter.api.Test;

public class CommandLineRendererTests {
    @Test
    void withNewCellAInXY_gettingCellBInSameXY_cellAEqualsCellB() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        CommandLineCell cellA = new CommandLineCell.newBlank();
        renderer.putCell(cellA, 0, 0);
        CommandLineCell cellB = renderer.getCell(0, 0);
        assertEquals(cellA, cellB);
    }
    @Test
    void withNewCommandLineRenderer_gettingWidthEqualsInitialWidth() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        assertEquals(1, renderer.getWidth);
    }
    @Test
    void withNewCommandLineRenderer_gettingHeightEqualsInitialHeight() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        assertEquals(2, renderer.getHeight);
    }
    @Test
    void withNewCommandLineRenderer_gettingRefreshRateEqualsInitialRefreshRate() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        assertEquals(30, renderer.getRefreshRate);
    }
    @Test
    void withExistingWidth_settingNewWidth_gettingWidthEqualsNewWidth() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setWidth(3);
        assertEquals(3, renderer.getWidth);
    }
    @Test
    void withExistingHeight_settingNewHeight_gettingHeightEqualsNewHeight() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setHeight(4);
        assertEquals(4, renderer.getHeight);
    }
    @Test
    void withExistingRefreshRate_settingNewRefreshRate_gettingRefreshRateEqualsNewRefreshRate() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setRefreshRate(60);
        assertEquals(60, renderer.getRefreshRate);
    }
    @Test
    void withNewCommandLineRenderer_settingNegativeWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(-1, 2, 30);
        assertEquals(1, renderer.getWidth);
    }
    @Test
    void withNewCommandLineRenderer_settingNegativeHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, -2, 30);
        assertEquals(1, renderer.getHeight);
    }
    @Test
    void withNewCommandLineRenderer_settingNegativeRefreshRate_gettingRefreshRateEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, -30);
        assertEquals(1, renderer.getRefreshRate);
    }
    @Test
    void withExistingWidth_settingNegativeWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setWidth(-3);
        assertEquals(1, renderer.getWidth);
    }
    @Test
    void withExistingHeight_settingNegativeHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setHeight(-4);
        assertEquals(1, renderer.getHeight);
    }
    @Test
    void withExistingRefreshRate_settingNegativeRate_gettingRefreshRateEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setRefreshRate(-60);
        assertEquals(1, renderer.getRefreshRate);
    }
    @Test
    void withNewCommandLineRenderer_settingZeroWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(0, 2, 30);
        assertEquals(1, renderer.getWidth);
    }
    @Test
    void withNewCommandLineRenderer_settingZeroHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 0, 30);
        assertEquals(1, renderer.getHeight);
    }
    @Test
    void withNewCommandLineRenderer_settingZeroRefreshRate_gettingRefreshRateEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 0);
        assertEquals(1, renderer.getRefreshRate);
    }
    @Test
    void withExistingWidth_settingZeroWidth_gettingWidthEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setWidth(0);
        assertEquals(1, renderer.getWidth);
    }
    @Test
    void withExistingHeight_settingZeroHeight_gettingHeightEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setHeight(0);
        assertEquals(1, renderer.getHeight);
    }
    @Test
    void withExistingRefreshRate_settingZeroRate_gettingRefreshRateEqualsOne() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        renderer.setRefreshRate(0);
        assertEquals(1, renderer.getRefreshRate);
    }
    @Test
    void withExistingCellInXY_puttingNullBoardInSameXY_gettingCellInSameXYEqualsExistingCell() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 2, 30);
        CommandLineCell cell = new CommandLineCell(ANSI.RED, ANSI.BLACK, 'E');
        renderer.putCell(cell, 0, 0);
        renderer.putBoard(null, 0, 0);
        assertEquals(cell, renderer.getCell(0, 0));
    }
    @Test
    void withExistingCellInXY_puttingBoardInSameXY_gettingCellInSameXYEqualsFirstCellInBoard() {
        CommandLineRenderer renderer = new CommandLineRenderer(1, 1, 30);
        CommandLineCell cell = CommandLineCell.newBlank();
        renderer.putCell(cell, 0, 0);
        Board board = new Board(1, 1);
        Civilian civilian = new Civilian(Team.RED, 0);
        board.board[0][0].addUnit(civilian);
        renderer.putBoard(board, 0, 0);
        assertEquals(civilian.toCommandLineCell(), renderer.getCell(0, 0));
    }
    @Test
    void withBoardInXY_gettingCellFromXYPlusBoardSize_cellEqualsLastCellInBoard() {}
    @Test
    void withCellAInFirstColumn_puttingCellBInNegativeX_cellInFirstColumnEqualsB() {}
    @Test
    void withCellAInLastColumn_puttingCellBInXGreaterThanBoardWidth_cellInLastColumnEqualsB() {}
    @Test
    void withCellAInFirstRow_puttingCellBInNegativeY_cellInFirstRowEqualsB() {}
    @Test
    void withCellAInLastRow_puttingCellBInYGreaterThanBoardWidth_cellInLastRowEqualsB() {}
}
