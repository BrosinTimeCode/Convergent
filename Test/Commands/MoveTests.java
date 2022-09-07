package Test.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Commands.CommandList;
import Commands.Move;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoveTests {

    @BeforeEach
    void initAll() {
        CommandList.initializeCommands();
    }

    @Test
    void moveAliasesGetMoveCommand() {
        List<String> aliases = Move.getInstance().getAliases();
        for (String alias : aliases) {
            assertEquals(Move.getInstance(), CommandList.getCommandFromAlias(alias));
        }
    }

}
