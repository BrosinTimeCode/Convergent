package com.brosintime.rts.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.brosintime.rts.Commands.Command.ArgStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SelectTests {

    @BeforeAll
    static void initAll() {
        CommandList.registerCommand(Select.instance());
    }

    @Test
    void selectCommand_withNoArguments_returnsNOARGS() {
        List<String> arguments = new ArrayList<>();
        Select.instance().setArguments(arguments);
        assertEquals(ArgStatus.NOARGS, Select.instance().validateArguments());
    }

    @Test
    void selectCommand_withMoreThanMaxArguments_returnsTOOMANY() {
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < Select.instance().maxArguments() + 1; i++) {
            arguments.add(Select.instance().defaultAlias());
        }
        Select.instance().setArguments(arguments);
        assertEquals(ArgStatus.TOOMANY, Select.instance().validateArguments());
    }

    @Test
    void selectCommand_withNegativeIntAsArgument_returnsBAD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("-1");
        Select.instance().setArguments(arguments);
        assertEquals(ArgStatus.BAD, Select.instance().validateArguments());
    }

    @Test
    void selectCommand_withPositiveIntAsArgument_returnsGOOD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("1");
        Select.instance().setArguments(arguments);
        assertEquals(ArgStatus.GOOD, Select.instance().validateArguments());
    }

    @Test
    void selectCommand_settingArguments_hasArguments() {
        List<String> inputArguments = new ArrayList<>();
        inputArguments.add("red");
        inputArguments.add("white");
        inputArguments.add("blue");
        Select.instance().setArguments(inputArguments);
        List<String> outputArguments = new ArrayList<>(Select.instance().getArguments());
        assertEquals(inputArguments, outputArguments);
    }

    @Test
    void selectCommandWithExistingArguments_settingNewArguments_onlyHasNewArguments() {
        List<String> argumentsSet1 = new ArrayList<>();
        argumentsSet1.add("red");
        argumentsSet1.add("white");
        argumentsSet1.add("blue");
        Select.instance().setArguments(argumentsSet1);
        List<String> argumentsSet2 = new ArrayList<>();
        argumentsSet1.add("one");
        argumentsSet1.add("two");
        Select.instance().setArguments(argumentsSet2);
        List<String> outputArguments = new ArrayList<>(Select.instance().getArguments());
        assertEquals(argumentsSet2, outputArguments);
        assertNotEquals(argumentsSet1, outputArguments);
        for (String arg : argumentsSet1) {
            assertFalse(outputArguments.contains(arg));
        }
    }

    @Test
    void selectCommandDefaultAlias_isARegisteredAlias() {
        assertTrue(CommandList.isAnAlias(Select.instance().defaultAlias()));
    }

    @Test
    void selectCommandInstance_isInstanceOfSelect() {
        assertTrue(Select.instance() instanceof Select);
    }
}
