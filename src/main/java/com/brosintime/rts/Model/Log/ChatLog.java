package com.brosintime.rts.Model.Log;

import com.brosintime.rts.Model.Commands.Command;
import com.brosintime.rts.Model.Commands.CommandList;
import com.brosintime.rts.Model.Commands.Help;
import com.brosintime.rts.Model.Player;
import com.brosintime.rts.View.Screen.Drawable.ColorCode;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the player’s chat log. It keeps a chronological record of system info,
 * error, and player chat messages that the player sends or receives and can see.
 */
public class ChatLog {

    private final List<ChatLogEntry> log = new ArrayList<>();
    private final int linesPerPage;

    /**
     * Constructs a new chat log instance.
     *
     * @param linesPerPage how many lines to split a list into pages as an integer of at least 5
     */
    public ChatLog(int linesPerPage) {
        this.linesPerPage = Math.max(linesPerPage, 5);
    }

    /**
     * Adds an entry to this log.
     *
     * @param log the entry
     */
    public void add(ChatLogEntry log) {
        this.log.add(log);
    }

    /**
     * Retrieves the size of this log.
     *
     * @return this log size
     */
    public int size() {
        return this.log.size();
    }

    /**
     * Retrieves the entry at the provided index of this log.
     *
     * @param index the index of the entry
     * @return the entry at the provided index to retrieve
     */
    public ChatLogEntry get(int index) {
        return this.log.get(index);
    }

    /**
     * Adds a chat message to this log. Color codes should not be added to the message unless added
     * by the player themselves.
     *
     * @param player  the author of the message
     * @param message the player message to add
     */
    public void chat(Player player, String message) {
        if (player == null || message == null) {
            return;
        }
        this.log.add(new ChatLogEntry(player, message));
    }

    /**
     * Adds a system info message to this log. The message will display yellow, so color codes
     * should only be added at discretion.
     *
     * @param message the system info message to add
     */
    public void info(String message) {
        if (message == null) {
            return;
        }
        this.log.add(new ChatLogEntry(ColorCode.YELLOW.fgColor() + message));
    }

    /**
     * Adds a list of system info messages to this log. All messages will display yellow, so color
     * codes should only be added at discretion. A horizontal rule and a title header is added
     * before the messages.
     * <p>If there is more than one page, instructions are automatically added to
     * the header. These instructions inform the player which page they’re on, the total page count,
     * and how to access additional pages with a command.
     *
     * @param messages the system messages to add; not null
     * @param title    the header to add to every page, <b>including</b> a single page; can be null
     * @param command  the command the player should type for other pages (e.g. "/help attack
     *                 [page]"); can be null
     */
    public void info(List<String> messages, String title, String command) {
        if (messages == null) {
            return;
        }
        if (messages.size() == 0) {
            return;
        }
        this.info(messages, title, command, 0);
    }


    /**
     * Accesses the full list of registered command aliases and adds them to this log as system info
     * messages. If the list does not fit on one page, only the first page as a sublist is added to
     * this log.
     * <p>A horizontal rule and a header with a title is automatically added before the list. If
     * there are multiple pages, instructions for the player to access other pages is added to the
     * header.
     */
    public void allAliases() {
        this.allAliases(0);
    }

    /**
     * Accesses the full list of registered command aliases and adds a sublist of them to this log
     * as system info messages. The sublist is determined by the provided page number.
     * <p>A horizontal rule and a header with a title is automatically added before the list. If
     * there are multiple pages, instructions for the player to access other pages is added to the
     * header.
     * <p>The page number is automatically set to the nearest valid page if it is out-of-bounds
     * (i.e. if page -1 is requested, page 0 is retrieved instead). If the full list of aliases fits
     * on one page, only the one page is retrieved regardless of the provided page number.
     *
     * @param page the page number to sublist the list of command aliases
     */
    public void allAliases(int page) {
        List<String> list = new ArrayList<>(CommandList.ALIASES.keySet());
        this.info(list, "List of commands", Help.instance().defaultAlias(), page);
    }

    /**
     * Adds a sublist of messages to this log as system info messages. The sublist is of the
     * provided list and is determined by the provided page number. All messages will display
     * yellow, so color codes should only be added at discretion.
     * <p>A horizontal rule and a header with the provided title (if not null) is automatically
     * added before the list. If there are multiple pages, provided instructions (if not null) for
     * the player to access other pages is added to the header.
     * <p>The page number is automatically set to the nearest valid page if it is out-of-bounds
     * (i.e. if page -1 is requested, page 0 is retrieved instead). If the full list fits on one
     * page, only the one page is retrieved regardless of the provided page number.
     *
     * @param messages the full list of messages to query; not null
     * @param title    the title to add to each page header, including single pages; can be null
     * @param command  the command the player should type for other pages (e.g. "/eat [page]"); can
     *                 be null
     * @param page     the page number to sublist the provided full list
     */
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

    /**
     * Accesses the full list of the usages of the provided command and adds them to this log as
     * system info messages. If the list does not fit on one page, only the first page as a sublist
     * is added to this log.
     * <p>A horizontal rule and a header with the command’s description is automatically added
     * before the list. If there are multiple pages, instructions for the player to access other
     * pages is added to the header.
     *
     * @param command the command to query the list of usages
     */
    public void usagesOf(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Cannot query list of usages of a null command");
        }
        this.usagesOf(command, 0);
    }

    /**
     * Accesses the full list of the usages of the provided command and adds a sublist of them to
     * this log as system info messages. The sublist is determined by the provided page number.
     * <p>A horizontal rule and a header with the command’s description is automatically added
     * before the list. If there are multiple pages, instructions for the player to access other
     * pages is added to the header.
     * <p>The page number is automatically set to the nearest valid page if it is out-of-bounds
     * (i.e. if page -1 is requested, page 0 is retrieved instead). If the full list fits on one
     * page, only the one page is retrieved regardless of the provided page number</p>
     *
     * @param command the command to query the list of usages
     * @param page    the page number to sublist the list of command usages
     */
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

    /**
     * Adds a system error message to this log informing the player that an invalid command was
     * entered.
     */
    public void invalidCommand() {
        this.error("Invalid command! Type “help” for a list of commands.");
    }

    /**
     * Adds a system error message to this log informing the player that a command with too many
     * arguments was entered. The provided command is used to retrieve and display the basic usage.
     *
     * @param command the command the player entered
     */
    public void tooManyArguments(Command command) {
        this.error("Too many arguments! Usage: " + command.basicUsage());
    }

    /**
     * Adds a summary of the provided command to this log as system info messages. The summary
     * includes the command’s name, its description, and a full list of its usages.
     *
     * @param command the command to display the summary for
     */
    public void commandInfo(Command command) {
        if (command == null) {
            throw new IllegalArgumentException("Cannot get general info of a null command");
        }
        this.info(command.name() + " - " + command.description());
        this.usagesOf(command);
    }

    /**
     * Adds a system error message to this log. The message will display red, so color codes should
     * only be added at discretion.
     *
     * @param message the system error message to add
     */
    public void error(String message) {
        if (message == null) {
            return;
        }
        this.info(ColorCode.DARK_RED.fgColor() + message);
    }

}
