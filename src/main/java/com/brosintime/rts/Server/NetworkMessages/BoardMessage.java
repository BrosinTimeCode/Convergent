package com.brosintime.rts.Server.NetworkMessages;

import com.brosintime.rts.Model.Board;

public class BoardMessage extends NetworkMessage {

    public final Board board;

    /**
     * Constructor for client. Takes a board message and updates the client-side board.
     * @param message Board message to be parsed.
     */
    public BoardMessage(String message) {
        super(NETWORK_MESSAGE_TYPE.BOARD);
        String[] components = message.split(":");
        board = new Board(components[1]);
    }

    /**
     * Constructor for server. Sends servers board to client.
     * @param board Board to send to the client.
     */
    public BoardMessage(Board board) {
        super(NETWORK_MESSAGE_TYPE.BOARD);
        this.board = board;
    }

    @Override
    public String getMessageString() {
        return message_type.type() + ":" + board.toString();
    }
}
