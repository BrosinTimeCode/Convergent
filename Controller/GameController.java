package Controller;

import Commands.Attack;
import Commands.BaseCommand;
import Commands.Move;
import Commands.Select;
import Units.BaseUnit;
import View.GameViewInterface;
import View.CommandLineInterface;
import Model.TestBoard;
import Model.Board;
import java.util.HashMap;
import java.util.Timer;

public class GameController {

    GameViewInterface viewInterface;
    Board board;
    private BaseUnit player1SelectedUnit;
    private HashMap<BaseUnit, BaseUnit> entitiesUnderAttack;

    public GameController(int viewType, int[] boardSize) {
        if (boardSize.length != 2) {
            board = new TestBoard();
        } else {
            board = new Board(boardSize[0], boardSize[1]);
        }
        switch (viewType) {
            default -> {
                viewInterface = new CommandLineInterface(board);
                viewInterface.initialize();
            }
        }
        entitiesUnderAttack = new HashMap<>();
    }

    public void initialize() {
        Timer timer = new Timer();
        long oneSecond = 1000;
        DamageEntityTask damageTask = new DamageEntityTask(this);
        timer.schedule(damageTask, 0, oneSecond);
    }

    public void handleUserInput() {
        viewInterface.displayHelp();
        while (true) {
            String userInput = viewInterface.getUserInput();
            BaseCommand userCommand = Parser.getCommand(userInput);
            if (userCommand == null) {
                viewInterface.displayInvalidCommand();
            }
        }
    }

    public boolean executeCommand(BaseCommand command) {
        if (command instanceof Move) {
            return executeMove((Move) command);
        } else if (command instanceof Select) {
            return executeSelect((Select) command);
        } else if(command instanceof Attack) {
            return executeAttack((Attack) command);
        }
        return false;
    }
    // TODO: make move remove attacked entity in damaged entities hash map
    public boolean executeMove(Move moveCommand) {
        return true;
    }

    public boolean executeAttack(Attack attackCommand) {
        String[] arguments = attackCommand.getArguments();
        return attackUnit(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
    }

    public boolean attackUnit(int row, int column) {
        // TODO: Fix for more than one player
        if(player1SelectedUnit == null) {
            return false;
        }
        if(checkBounds(row, column)) {
            entitiesUnderAttack.put(player1SelectedUnit, board.getUnit(row, column));
            return true;
        }
        return false;
    }

    public void killUnit(BaseUnit attacker, BaseUnit deadUnit) {
        board.killUnit(deadUnit.getId());
        entitiesUnderAttack.remove(attacker);
    }

    public boolean executeSelect(Select selectCommand) {
        String[] arguments = selectCommand.getArguments();
        return selectUnit(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));
    }

    public boolean selectUnit(int row, int column) {
        if (checkBounds(row, column)) {
            player1SelectedUnit = board.getUnit(row, column);
            return true;
        }
        return false;
    }

    private boolean checkBounds(int row, int column) {
        return !(row > board.getBoardHeight() || row < 0 || column > board.getBoardWidth()
          || column < 0);
    }

    public HashMap<BaseUnit, BaseUnit> getEntitiesUnderAttack() {
        return entitiesUnderAttack;
    }
}
