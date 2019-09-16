package top.andnux.net.netstate;

public interface NetStateListener {

    void onConnect(NetState state);

    void onDisConnect();
}
