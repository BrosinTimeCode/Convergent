package Commands;

public class Alias implements Comparable<Alias> {

    private final String alias;
    private Command command;

    public Alias(String alias, Command command) {
        this.alias = alias;
        this.command = command;
    }

    @Override
    public String toString() {
        return this.alias;
    }

    @Override
    public int compareTo(Alias a) {
        return this.alias.compareTo(a.alias);
    }

    public Command getCommand() {
        return this.command;
    }
}
