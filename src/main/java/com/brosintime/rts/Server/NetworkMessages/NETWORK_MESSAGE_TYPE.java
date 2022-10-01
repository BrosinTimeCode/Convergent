package Server.NetworkMessages;

public enum NETWORK_MESSAGE_TYPE {
    MOVE(1);

    /**
     * Integer representation of the networkMessageType
     */
    private final int numCode;

    NETWORK_MESSAGE_TYPE(int numCode) {
        this.numCode = numCode;
    }

    public static NETWORK_MESSAGE_TYPE fromValue(int value) {
        switch(value) {
            case 1:
                return NETWORK_MESSAGE_TYPE.MOVE;
        }
        return null;
    }

    public int type() {
        return numCode;
    }

}
