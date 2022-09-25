package Server.NetworkMessages;

public abstract class NetworkMessage {

    public final NETWORK_MESSAGE_TYPE message_type;

    /**
     * Returns string representation of message to send to server/client
     */
    public abstract String getMessageString();

    public NetworkMessage(NETWORK_MESSAGE_TYPE type) {
        message_type = type;
    }

}
