package com.brosintime.rts.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;

public class CommandListTests {

    @Test
    void commandsInCommandList_aliasesGetAppropriateCommand() {
        for (Command command : CommandList.COMMANDS) {
            List<String> aliases = command.aliases();
            for (String alias : aliases) {
                assertEquals(command, CommandList.fromAlias(alias));
            }
        }
    }

    @Test
    void commandAliasesAsUserInput_returnsCommand() {
        for (String aliasAsInput : CommandList.ALIASES.keySet()) {
            Command command = CommandList.fromInput(aliasAsInput);
            assertNotNull(command);
        }
    }
}
