package Test.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Commands.Attack;
import Commands.CommandList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AttackTests {

    @BeforeEach
    void initAll() {
        CommandList.initializeCommands();
    }

    @Test
    void attackAliasesGetAttackCommand() {
        List<String> aliases = Attack.getInstance().getAliases();
        for (String alias : aliases) {
            assertEquals(Attack.getInstance(), CommandList.getCommandFromAlias(alias));
        }
    }

}
