package top.andnux.utils.netstate;

public interface NetStateListener {

    void onConnect(NetState state);

    void onDisConnect();
}
