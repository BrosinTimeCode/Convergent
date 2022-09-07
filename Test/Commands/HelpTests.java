package Test.Commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Commands.Command;
import Commands.CommandList;
import Commands.Help;
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
    void noArgumentsGetsAllCommands() {
        List<String> arguments = new ArrayList<>();
        Command command = CommandList.getCommandFromAlias("help");
        command.setArguments(arguments);

    }
}
