package Test.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Commands.Attack;
import Commands.CommandList;
import Commands.CommandList.ArgStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AttackTests {

    @BeforeEach
    void initAll() {
        CommandList.initializeCommands();
    }

    @Test
    void attackCommand_withNoArguments_returnsNOARGS() {
        List<String> arguments = new ArrayList<>();
        Attack.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.NOARGS, Attack.getInstance().validateArguments());
    }

    @Test
    void attackCommand_withMoreThanMaxArguments_returnsTOOMANY() {
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < Attack.getInstance().getMaxArguments() + 1; i++) {
            arguments.add(Attack.getInstance().getDefaultAlias());
        }
        Attack.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.TOOMANY, Attack.getInstance().validateArguments());
    }

    @Test
    void attackCommand_withNegativeIntAsArgument_returnsBAD() {
        List<String> arguments = new ArrayList<>();
        arguments.add("-1");
        Attack.getInstance().setArguments(arguments);
        assertEquals(ArgStatus.BAD, Attack.getInstance().validateArguments());
    }

    @Test
    void attackCommand_withPositiveIntAsArgument_returnsGOOD() {
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < Attack.getInstance().getMaxArguments(); i++) {
            arguments.add("1");
            Attack.getInstance().setArguments(arguments);
            assertEquals(ArgStatus.GOOD, Attack.getInstance().validateArguments());
        }
    }

    @Test
    void attackCommand_settingArguments_hasArguments() {
        List<String> inputArguments = new ArrayList<>();
        inputArguments.add("red");
        inputArguments.add("white");
        inputArguments.add("blue");
        Attack.getInstance().setArguments(inputArguments);
        List<String> outputArguments = new ArrayList<>(Attack.getInstance().getArguments());
        assertEquals(inputArguments, outputArguments);
    }

    @Test
    void attackCommandWithExistingArguments_settingNewArguments_onlyHasNewArguments() {
        List<String> argumentsSet1 = new ArrayList<>();
        argumentsSet1.add("red");
        argumentsSet1.add("white");
        argumentsSet1.add("blue");
        Attack.getInstance().setArguments(argumentsSet1);
        List<String> argumentsSet2 = new ArrayList<>();
        argumentsSet1.add("one");
        argumentsSet1.add("two");
        Attack.getInstance().setArguments(argumentsSet2);
        List<String> outputArguments = new ArrayList<>(Attack.getInstance().getArguments());
        assertEquals(argumentsSet2, outputArguments);
        assertNotEquals(argumentsSet1, outputArguments);
        for (String arg : argumentsSet1) {
            assertFalse(outputArguments.contains(arg));
        }
    }

    @Test
    void attackCommandDefaultAlias_isARegisteredAlias() {
        assertTrue(CommandList.isAnAlias(Attack.getInstance().getDefaultAlias()));
    }

    @Test
    void attackCommandInstance_isInstanceOfAttack() {
        assertTrue(Attack.getInstance() instanceof Attack);
    }
}
