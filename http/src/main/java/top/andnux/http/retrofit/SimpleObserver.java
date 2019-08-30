package top.andnux.http.retrofit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class SimpleObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
        System.out.println("onSubscribe:" + d);
    }

    @Override
    public void onError(Throwable e) {
        System.out.println("onError:" + e);
    }

    @Override
    public void onComplete() {
        System.out.println("onComplete");
    }
}
