package com.brosintime.rts.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.brosintime.rts.Commands.CommandList.ArgStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoveTests {

    @BeforeEach
    void initAll() {
        CommandList.initializeCommands();
    }

    @Test
    void moveCommand_withNoArguments_returnsNOARGS() {
        List<String> arguments = new ArrayList<>();
        Move.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.NOARGS, Move.getInstance().validateArguments());
    }

    @Test
    void moveCommand_withMoreThanMaxArguments_returnsTOOMANY() {
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < Move.getInstance().getMaxArguments() + 1; i++) {
            arguments.add(Move.getInstance().getDefaultAlias());
        }
        Move.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.TOOMANY, Move.getInstance().validateArguments());
    }

    @Test
    void moveCommand_withNegativeIntAsArgument_returnsBAD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("-1");
        Move.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.BAD, Move.getInstance().validateArguments());
    }

    @Test
    void moveCommand_withPositiveIntAsArgument_returnsGOOD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("1");
        Move.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.GOOD, Move.getInstance().validateArguments());
    }

    @Test
    void moveCommand_settingArguments_hasArguments() {
        List<String> inputArguments = new ArrayList<>();
        inputArguments.add("red");
        inputArguments.add("white");
        inputArguments.add("blue");
        Move.getInstance().setArguments(inputArguments);
        List<String> outputArguments = new ArrayList<>(Move.getInstance().getArguments());
        assertEquals(inputArguments, outputArguments);
    }

    @Test
    void moveCommandWithExistingArguments_settingNewArguments_onlyHasNewArguments() {
        List<String> argumentsSet1 = new ArrayList<>();
        argumentsSet1.add("red");
        argumentsSet1.add("white");
        argumentsSet1.add("blue");
        Move.getInstance().setArguments(argumentsSet1);
        List<String> argumentsSet2 = new ArrayList<>();
        argumentsSet1.add("one");
        argumentsSet1.add("two");
        Move.getInstance().setArguments(argumentsSet2);
        List<String> outputArguments = new ArrayList<>(Move.getInstance().getArguments());
        assertEquals(argumentsSet2, outputArguments);
        assertNotEquals(argumentsSet1, outputArguments);
        for (String arg : argumentsSet1) {
            assertFalse(outputArguments.contains(arg));
        }
    }

    @Test
    void moveCommandDefaultAlias_isARegisteredAlias() {
        assertTrue(CommandList.isAnAlias(Move.getInstance().getDefaultAlias()));
    }

    @Test
    void moveCommandInstance_isInstanceOfMove() {
        assertTrue(Move.getInstance() instanceof Move);
    }
}
