package Commands;

import java.util.List;
import java.util.Map;

public abstract class Command {

    public enum ArgStatus {
        GOOD,
        TOOMANY,
        NOARGS,
        BAD
    }

    public abstract void setArguments(List<String> args);

    public abstract List<String> getArguments();

    public abstract Map<Integer, String> getUsages();

    public abstract String getName();

    public abstract String getDefaultAlias();

    public abstract String getBasicUsage();

    public abstract String getDescription();

    public abstract boolean hasTooManyArguments();

    public abstract ArgStatus validateArguments();

    public abstract String getArgument(int index) throws IndexOutOfBoundsException;

}
