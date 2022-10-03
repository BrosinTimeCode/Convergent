package com.brosintime.rts.Server.NetworkMessages;

public abstract class NetworkMessage {

    public final NETWORK_MESSAGE_TYPE message_type;
    public int length;

    /**
     * Returns string representation of message to send to server/client
     */
    public abstract String messageString();

    public NetworkMessage(NETWORK_MESSAGE_TYPE type) {
        message_type = type;
    }

}
