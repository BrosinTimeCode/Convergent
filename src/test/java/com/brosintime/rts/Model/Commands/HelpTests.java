package com.brosintime.rts.Model.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.brosintime.rts.Model.Commands.Command.ArgStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class HelpTests {

    @BeforeAll
    static void initAll() {
        CommandList.registerCommand(Help.instance());
    }

    @Test
    void helpCommand_withNoArguments_returnsNOARGS() {
        List<String> arguments = new ArrayList<>();
        Help.instance().setArguments(arguments);
        assertEquals(ArgStatus.NOARGS, Help.instance().validateArguments());
    }

    @Test
    void helpCommand_withMoreThanMaxArguments_returnsTOOMANY() {
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < Help.instance().maxArguments() + 1; i++) {
            arguments.add(Help.instance().defaultAlias());
        }
        Help.instance().setArguments(arguments);
        assertEquals(ArgStatus.TOOMANY, Help.instance().validateArguments());
    }

    @Test
    void helpCommand_withNegativeIntAsArgument_returnsBAD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("-1");
        Help.instance().setArguments(arguments);
        assertEquals(ArgStatus.BAD, Help.instance().validateArguments());
    }

    @Test
    void helpCommand_withPositiveIntAsArgument_returnsGOOD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("1");
        Help.instance().setArguments(arguments);
        assertEquals(ArgStatus.GOOD, Help.instance().validateArguments());
    }

    @Test
    void helpCommand_withCommandAliasAsArgument_returnsGOOD() {
        List<String> arguments = new ArrayList<>();
        for (String alias : CommandList.ALIASES.keySet()) {
            arguments.add(alias);
            Help.instance().setArguments(arguments);
            assertEquals(ArgStatus.GOOD, Help.instance().validateArguments());
            arguments.clear();
        }
    }

    @Test
    void helpCommand_settingArguments_hasArguments() {
        List<String> inputArguments = new ArrayList<>();
        inputArguments.add("red");
        inputArguments.add("white");
        inputArguments.add("blue");
        Help.instance().setArguments(inputArguments);
        List<String> outputArguments = new ArrayList<>(Help.instance().arguments());
        assertEquals(inputArguments, outputArguments);
    }

    @Test
    void helpCommandWithExistingArguments_settingNewArguments_onlyHasNewArguments() {
        List<String> argumentsSet1 = new ArrayList<>();
        argumentsSet1.add("red");
        argumentsSet1.add("white");
        argumentsSet1.add("blue");
        Help.instance().setArguments(argumentsSet1);
        List<String> argumentsSet2 = new ArrayList<>();
        argumentsSet1.add("one");
        argumentsSet1.add("two");
        Help.instance().setArguments(argumentsSet2);
        List<String> outputArguments = new ArrayList<>(Help.instance().arguments());
        assertEquals(argumentsSet2, outputArguments);
        assertNotEquals(argumentsSet1, outputArguments);
        for (String arg : argumentsSet1) {
            assertFalse(outputArguments.contains(arg));
        }
    }

    @Test
    void helpCommandDefaultAlias_isARegisteredAlias() {
        assertTrue(CommandList.isAnAlias(Help.instance().defaultAlias()));
    }

    @Test
    void helpCommandInstance_isInstanceOfHelp() {
        assertTrue(Help.instance() instanceof Help);
    }
}
