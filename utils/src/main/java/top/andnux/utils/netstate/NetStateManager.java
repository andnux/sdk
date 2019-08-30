package top.andnux.utils.netstate;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import top.andnux.utils.Utils;
import top.andnux.utils.netstate.annotation.NetSupport;

public class NetStateManager implements NetStateListener {

    private static final NetStateManager ourInstance = new NetStateManager();
    private boolean isFirst = true;

    public static NetStateManager getInstance() {
        return ourInstance;
    }

    private Application mApplication;
    private NetStateListener mStateListener;
    private NetStateReceiver mNetBroadcastReceiver;
    private Map<Object, List<NetStateBean>> methodMap = new ArrayMap<>();
    private static ArrayList<NetStateListener> mNetChangeObservers = new ArrayList<>();

    private NetStateManager() {
        mNetBroadcastReceiver = new NetStateReceiver();
        mNetBroadcastReceiver.setNetListener(this);
    }

    public NetStateListener getStateListener() {
        return mStateListener;
    }

    public void setStateListener(NetStateListener stateListener) {
        mStateListener = stateListener;
    }

    public static void init(Application context) {
        getInstance().mApplication = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager service = (ConnectivityManager) NetStateManager.getInstance().getApplication().
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = null;
            builder = new NetworkRequest.Builder();
            if (service != null) {
                service.requestNetwork(builder.build(),
                        new NetworkCallback(getInstance()));
            }
        } else {
            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            context.registerReceiver(getInstance().mNetBroadcastReceiver, filter);
        }
    }

    public Application getApplication() {
        return mApplication;
    }

    public void registerObserver(Object object) {
        if (mApplication == null){
            init(Utils.getApp());
        }
        List<NetStateBean> methodBeans = methodMap.get(object);
        if (methodBeans == null) {
            methodBeans = findAnnotationMethod(object);
            methodMap.put(object, methodBeans);
        }
    }

    /**
     * 添加网络监听
     *
     * @param observer
     */
    public void registerNetStateListener(NetStateListener observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<>();
        }
        mNetChangeObservers.add(observer);
        if (isFirst) {
            isFirst = false;
            NetState netState = NetUtil.getNetState();
            if (netState == NetState.NONE && observer != null) {
                observer.onConnect(NetState.NONE);
            }
        }
    }

    /**
     * 移除网络监听
     *
     * @param observer
     */
    public void unRegisterNetStateListener(NetStateListener observer) {
        if (mNetChangeObservers != null) {
            mNetChangeObservers.remove(observer);
        }
    }

    private List<NetStateBean> findAnnotationMethod(Object object) {
        List<NetStateBean> list = new ArrayList<>();
        Class<?> aClass = object.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            NetSupport annotation = method.getAnnotation(NetSupport.class);
            if (annotation == null) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new IllegalArgumentException("NetSupport 注解得方法只能有一个参数");
            }
            list.add(new NetStateBean(annotation.value(), parameterTypes[0], method));
        }
        return list;
    }


    public void unRegisterObserver(Object object) {
        List<NetStateBean> methodBeans = methodMap.get(object);
        if (methodBeans != null) {
            methodMap.remove(object);
        }
        Log.e("TAG", "注销成功");
    }

    public void unRegisterAllObservers() {
        if (!methodMap.isEmpty()) {
            methodMap.clear();
        }
        NetStateManager.getInstance().getApplication()
                .unregisterReceiver(mNetBroadcastReceiver);
        methodMap = null;
        Log.e("TAG", "全部注销成功");
    }

    @Override
    public void onConnect(NetState state) {
        postMessage(state);
        if (mStateListener != null) mStateListener.onConnect(state);
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetStateListener observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    observer.onConnect(state);
                }
            }
        }
    }

    private void postMessage(NetState state) {
        try {
            Set<Object> set = methodMap.keySet();
            for (Object object : set) {
                List<NetStateBean> list = methodMap.get(object);
                if (list != null) {
                    for (NetStateBean bean : list) {
                        Method method = bean.getMethod();
                        switch (bean.getNetState()) {
                            case AUTO:
                                if (state == NetState.WIFI
                                        || state == NetState.MOBILE
                                        || state == NetState.NONE) {
                                    invoke(method, object, state);
                                }
                                break;
                            case WIFI:
                                if (state == NetState.WIFI || state == NetState.NONE) {
                                    invoke(method, object, state);
                                }
                                break;
                            case MOBILE:
                                if (state == NetState.MOBILE || state == NetState.NONE) {
                                    invoke(method, object, state);
                                }
                                break;
                            case NONE:
                                if (state == NetState.NONE) {
                                    invoke(method, object, state);
                                }
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invoke(Method method, Object object, NetState state) {
        try {
            method.setAccessible(true);
            method.invoke(object, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDisConnect() {
        postMessage(NetState.NONE);
        if (mStateListener != null) mStateListener.onDisConnect();
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetStateListener observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    observer.onConnect(NetState.NONE);
                }
            }
        }
    }
}
