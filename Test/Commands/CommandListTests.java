package Test.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import Commands.Command;
import Commands.CommandList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CommandListTests {

    @Test
    void commandsInCommandList_aliasesGetAppropriateCommand() {
        CommandList.initializeCommands();
        for (Command command : CommandList.COMMANDS) {
            List<String> aliases = command.getAliases();
            for (String alias : aliases) {
                assertEquals(command, CommandList.getCommandFromAlias(alias));
            }
        }
    }

    @Test
    void commandAliasesAsUserInput_returnsCommand() {
        for (String aliasAsInput : CommandList.ALIASES.keySet()) {
            Command command = CommandList.getCommandFromInput(aliasAsInput);
            assertNotNull(command);
        }
    }
}
