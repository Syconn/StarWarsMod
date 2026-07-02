package mod.syconn.swm.api.network;

public enum PacketDirection {

    PLAY_SERVER_BOUND(false, true),
    PLAY_CLIENT_BOUND(true, false);

    private final boolean client;
    private final boolean server;

    PacketDirection(boolean client, boolean server) {
        this.client = client;
        this.server = server;
    }

    public boolean isClient() {
        return this.client;
    }

    public boolean isServer() {
        return this.server;
    }
}
