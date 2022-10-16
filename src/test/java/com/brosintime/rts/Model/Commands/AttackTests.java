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

public class AttackTests {

    @BeforeAll
    static void initAll() {
        CommandList.registerCommand(Attack.instance());
    }

    @Test
    void attackCommand_withNoArguments_returnsNOARGS() {
        List<String> arguments = new ArrayList<>();
        Attack.instance().setArguments(arguments);
        assertEquals(ArgStatus.NOARGS, Attack.instance().validateArguments());
    }

    @Test
    void attackCommand_withMoreThanMaxArguments_returnsTOOMANY() {
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < Attack.instance().maxArguments() + 1; i++) {
            arguments.add(Attack.instance().defaultAlias());
        }
        Attack.instance().setArguments(arguments);
        assertEquals(ArgStatus.TOOMANY, Attack.instance().validateArguments());
    }

    @Test
    void attackCommand_withNegativeIntAsArgument_returnsBAD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("-1");
        Attack.instance().setArguments(arguments);
        assertEquals(ArgStatus.BAD, Attack.instance().validateArguments());
    }

    @Test
    void attackCommand_withPositiveIntAsArgument_returnsGOOD() {
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < Attack.instance().maxArguments(); i++) {
            arguments.add("1");
            Attack.instance().setArguments(arguments);
            assertEquals(ArgStatus.GOOD, Attack.instance().validateArguments());
        }
    }

    @Test
    void attackCommand_settingArguments_hasArguments() {
        List<String> inputArguments = new ArrayList<>();
        inputArguments.add("red");
        inputArguments.add("white");
        inputArguments.add("blue");
        Attack.instance().setArguments(inputArguments);
        List<String> outputArguments = new ArrayList<>(Attack.instance().arguments());
        assertEquals(inputArguments, outputArguments);
    }

    @Test
    void attackCommandWithExistingArguments_settingNewArguments_onlyHasNewArguments() {
        List<String> argumentsSet1 = new ArrayList<>();
        argumentsSet1.add("red");
        argumentsSet1.add("white");
        argumentsSet1.add("blue");
        Attack.instance().setArguments(argumentsSet1);
        List<String> argumentsSet2 = new ArrayList<>();
        argumentsSet1.add("one");
        argumentsSet1.add("two");
        Attack.instance().setArguments(argumentsSet2);
        List<String> outputArguments = new ArrayList<>(Attack.instance().arguments());
        assertEquals(argumentsSet2, outputArguments);
        assertNotEquals(argumentsSet1, outputArguments);
        for (String arg : argumentsSet1) {
            assertFalse(outputArguments.contains(arg));
        }
    }

    @Test
    void attackCommandDefaultAlias_isARegisteredAlias() {
        assertTrue(CommandList.isAnAlias(Attack.instance().defaultAlias()));
    }

    @Test
    void attackCommandInstance_isInstanceOfAttack() {
        assertTrue(Attack.instance() instanceof Attack);
    }
}
