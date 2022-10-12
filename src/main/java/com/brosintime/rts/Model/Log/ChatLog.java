package com.brosintime.rts.Model.Log;

import com.brosintime.rts.Model.Commands.Command;
import com.brosintime.rts.Model.Commands.CommandList;
import com.brosintime.rts.Model.Commands.Help;
import com.brosintime.rts.Model.Player;
import com.brosintime.rts.View.Screen.Drawable.ColorCode;
import java.util.ArrayList;
import java.util.List;

public class ChatLog {

    private final List<ChatLogEntry> log = new ArrayList<>();
    private final int linesPerPage;

    public ChatLog(int linesPerPage) {
        this.linesPerPage = Math.max(linesPerPage, 5);
    }

    public void add(ChatLogEntry log) {
        this.log.add(log);
    }

    public int size() {
        return this.log.size();
    }

    public ChatLogEntry get(int index) {
        return this.log.get(index);
    }

    public void chat(Player player, String message) {
        if (player == null || message == null) {
            return;
        }
        this.log.add(new ChatLogEntry(player, message));
    }

    public void info(String message) {
        if (message == null) {
            return;
        }
        this.log.add(new ChatLogEntry(ColorCode.YELLOW.fgColor() + message));
    }

    public void info(List<String> messages, String title, String command) {
        if (messages == null) {
            return;
        }
        if (messages.size() == 0) {
            return;
        }
        this.info(messages, title, command, 0);
    }


    public void allAliases() {
        this.allAliases(0);
    }

    public void allAliases(int page) {
        List<String> list = new ArrayList<>(CommandList.ALIASES.keySet());
        this.info(list, "List of commands", Help.instance().defaultAlias(), page);
    }

    public void info(List<String> messages, String title, String command, int page) {
        if (messages == null) {
            return;
        }
        if (messages.size() == 0) {
            return;
        }
        page = Math.max(page, 1);

        if (messages.size() < this.linesPerPage) {
            this.info("-".repeat(40));
            if (title != null) {
                this.info(title);
            }
            for (String message : messages) {
                this.info(message);
            }
            return;
        }

        final int TOTAL_PAGES = (int) Math.ceil((double) messages.size() / this.linesPerPage);

        page = Math.min(page, TOTAL_PAGES);

        this.info("-".repeat(40));

        final StringBuilder header = new StringBuilder();
        if (title != null) {
            header.append(title).append(" - ");
        }
        header.append("Page (").append(page).append('/').append(TOTAL_PAGES).append(')');
        if (command != null) {
            header.append(" - Type \"").append(command).append(" [page]\"");
        }
        this.info(header.toString());

        List<String> sublist = messages.subList((page - 1) * this.linesPerPage,
            page == TOTAL_PAGES ? messages.size() : page * this.linesPerPage);
        for (String message : sublist) {
            this.info(message);
        }
    }

    public void usagesOf(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Cannot query list of usages of a null command");
        }
        this.usagesOf(command, 0);
    }

    public void usagesOf(Command command, int page) {
        if (command == null) {
            throw new IllegalArgumentException("Cannot query list of usages of a null command");
        }
        List<String> list = new ArrayList<>();
        list.add(command.description() + " Usages:");
        for (String usage : command.usages()) {
            list.add(command.defaultAlias() + " " + usage);
        }
        this.info(list, command.name(), Help.instance().defaultAlias() + command.defaultAlias(),
            page);
    }

    public void invalidCommand() {
        this.error("Invalid command! Type “help” for a list of commands.");
    }

    public void tooManyArguments(Command command) {
        this.error("Too many arguments! Usage: " + command.basicUsage());
    }

    public void commandInfo(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Cannot get general info of a null command");
        }
        this.info(command.name() + " - " + command.description());
        this.usagesOf(command);
    }

    public void error(String message) {
        if (message == null) {
            return;
        }
        this.info(ColorCode.DARK_RED.fgColor() + message);
    }

}
