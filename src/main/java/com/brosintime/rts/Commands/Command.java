package com.brosintime.rts.Commands;

import java.util.List;

public interface Command extends CommandList {

    enum ArgStatus {
        GOOD, TOOMANY, NOARGS, BAD
    }

    List<String> getArguments();

    void setArguments(List<String> args);


    String name();

    String defaultAlias();

    String basicUsage();

    String description();

    boolean hasTooManyArguments();

    ArgStatus validateArguments();

    String getArgument(int index) throws IndexOutOfBoundsException;

    byte maxArguments();

}
