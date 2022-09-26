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

public class HelpTests {

    @BeforeEach
    void initAll() {
        CommandList.initializeCommands();
    }

    @Test
    void helpCommand_withNoArguments_returnsNOARGS() {
        List<String> arguments = new ArrayList<>();
        Help.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.NOARGS, Help.getInstance().validateArguments());
    }

    @Test
    void helpCommand_withMoreThanMaxArguments_returnsTOOMANY() {
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < Help.getInstance().getMaxArguments() + 1; i++) {
            arguments.add(Help.getInstance().getDefaultAlias());
        }
        Help.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.TOOMANY, Help.getInstance().validateArguments());
    }

    @Test
    void helpCommand_withNegativeIntAsArgument_returnsBAD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("-1");
        Help.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.BAD, Help.getInstance().validateArguments());
    }

    @Test
    void helpCommand_withPositiveIntAsArgument_returnsGOOD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("1");
        Help.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.GOOD, Help.getInstance().validateArguments());
    }

    @Test
    void helpCommand_withCommandAliasAsArgument_returnsGOOD() {
        List<String> arguments = new ArrayList<>();
        for (String alias : CommandList.ALIASES.keySet()) {
            arguments.add(alias);
            Help.getInstance().setArguments(arguments);
            assertEquals(ArgStatus.GOOD, Help.getInstance().validateArguments());
            arguments.clear();
        }
    }

    @Test
    void helpCommand_settingArguments_hasArguments() {
        List<String> inputArguments = new ArrayList<>();
        inputArguments.add("red");
        inputArguments.add("white");
        inputArguments.add("blue");
        Help.getInstance().setArguments(inputArguments);
        List<String> outputArguments = new ArrayList<>(Help.getInstance().getArguments());
        assertEquals(inputArguments, outputArguments);
    }

    @Test
    void helpCommandWithExistingArguments_settingNewArguments_onlyHasNewArguments() {
        List<String> argumentsSet1 = new ArrayList<>();
        argumentsSet1.add("red");
        argumentsSet1.add("white");
        argumentsSet1.add("blue");
        Help.getInstance().setArguments(argumentsSet1);
        List<String> argumentsSet2 = new ArrayList<>();
        argumentsSet1.add("one");
        argumentsSet1.add("two");
        Help.getInstance().setArguments(argumentsSet2);
        List<String> outputArguments = new ArrayList<>(Help.getInstance().getArguments());
        assertEquals(argumentsSet2, outputArguments);
        assertNotEquals(argumentsSet1, outputArguments);
        for (String arg : argumentsSet1) {
            assertFalse(outputArguments.contains(arg));
        }
    }

    @Test
    void helpCommandDefaultAlias_isARegisteredAlias() {
        assertTrue(CommandList.isAnAlias(Help.getInstance().getDefaultAlias()));
    }

    @Test
    void helpCommandInstance_isInstanceOfHelp() {
        assertTrue(Help.getInstance() instanceof Help);
    }
}
