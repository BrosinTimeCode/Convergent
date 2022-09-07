package Test.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Commands.Attack;
import Commands.Command;
import Commands.CommandList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CommandListTests {

    @Test
    void aliasesGetAppropriateCommand() {
        CommandList.initializeCommands();
        for (Command command : CommandList.COMMANDS) {
            List<String> aliases = command.getAliases();
            for (String alias : aliases) {
                assertEquals(command, CommandList.getCommandFromAlias(alias));
            }
        }
    }
}
