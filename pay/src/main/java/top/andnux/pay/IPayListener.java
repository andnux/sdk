package top.andnux.pay;

public interface IPayListener {

    void onSuccess();

    void onError();

    void onCancel();
}
