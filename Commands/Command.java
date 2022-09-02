package Commands;

import java.util.List;
import java.util.Map;

public abstract class Command {

    public abstract void setArguments(List<String> args);
    public abstract List<String> getArguments();
    public abstract Map<Integer, String> getUsages();
    public abstract String getDescription();
    public abstract boolean hasTooManyArguments();
    public abstract byte validateArguments();

}
