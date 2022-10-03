package com.brosintime.rts.Server.NetworkMessages;

public class MoveMessage extends NetworkMessage {

    public final int unitID;
    public final int targetID;
    public final int xCoordinate;
    public final int yCoordinate;

    /**
     * Constructor for server. Server receives the message and creates a move message.
     *
     * @param message Move message to be parsed.
     */
    public MoveMessage(String message) {
        super(NETWORK_MESSAGE_TYPE.MOVE);
        String[] components = message.split(":");
        this.unitID = Integer.parseInt(components[1]);
        this.targetID = Integer.parseInt(components[2]);
        this.xCoordinate = Integer.parseInt(components[3]);
        this.yCoordinate = Integer.parseInt(components[4]);
    }

    /**
     * Constructor for client. Creates a message given parameters.
     *
     * @param unitID      An integer representing the ID of the unit being moved.
     * @param targetID    An integer representing the ID of the unit being toward in applicable.
     * @param xCoordinate An integer representing the x coordinate on the board being moved to.
     * @param yCoordinate An integer representing the y coordinate on the board being moved to.
     */
    public MoveMessage(int unitID, int targetID, int xCoordinate, int yCoordinate) {
        super(NETWORK_MESSAGE_TYPE.MOVE);
        this.unitID = unitID;
        this.targetID = targetID;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        length = this.messageString().getBytes().length;
    }


    @Override
    public String messageString() {
        return message_type.type() + ":" + unitID + ":" + targetID + ":" + xCoordinate + ":"
            + yCoordinate;
    }
}
