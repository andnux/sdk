package top.andnux.mvp.base;

public interface BaseView {
    /**
     * 提示成功消息
     *
     * @param msg
     */
    void toastSuccess(String msg);

    /**
     * 提示错误消息
     *
     * @param msg
     */
    void toastError(String msg);

    /**
     * 提示普通消息
     *
     * @param msg
     */
    void toastInfo(String msg);

    /**
     * 显示错误页面
     *
     * @param e
     */
    void showErrorView(Throwable e);

    /**
     * 显示未登录页面
     *
     * @param msg
     */
    void showNoLoginView(String msg);

    /**
     * 显示空数据页面
     *
     * @param msg
     */
    void showEmptyView(String msg);

    /**
     * 显示加载页面
     *
     * @param msg
     */
    void showLoadingView(String msg);

    /**
     * 显示内容页面
     */
    void showContentView();
}
