package top.andnux.utils.netstate;

import java.lang.reflect.Method;

public class NetStateBean {

    private NetState mNetState;
    private Class<?> type;
    private Method mMethod;

    public NetStateBean() {
    }

    public NetStateBean(NetState mNetState, Class<?> type, Method mMethod) {
        this.mNetState = mNetState;
        this.type = type;
        this.mMethod = mMethod;
    }

    public NetState getNetState() {
        return mNetState;
    }

    public void setNetState(NetState mNetState) {
        this.mNetState = mNetState;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Method getMethod() {
        return mMethod;
    }

    public void setMethod(Method mMethod) {
        this.mMethod = mMethod;
    }
}
