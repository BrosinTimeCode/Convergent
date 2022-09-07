package Test.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Commands.CommandList;
import Commands.Select;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SelectTests {

    @BeforeEach
    void initAll() {
        CommandList.initializeCommands();
    }

    @Test
    void selectAliasesGetSelectCommand() {
        List<String> aliases = Select.getInstance().getAliases();
        for (String alias : aliases) {
            assertEquals(Select.getInstance(), CommandList.getCommandFromAlias(alias));
        }
    }

}
