package com.eshuix.javademo.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

public abstract class BaseMvpFragment<V extends IView, P extends IPresenter<V>> extends BaseFragment implements IView {

    public P presenter;

    protected abstract P createPresenter();

    @Override
    public void initView(View view) {
        presenter = createPresenter();
        presenter.bindingView((V) this);
    }

    @Override
    public void startActivity(Class<?> clz) {
        super.startActivity(clz);
    }

    @Override
    public void startActivity(Class<?> clz, @NonNull Bundle bundle) {
        super.startActivity(clz, bundle);
    }

    @Override
    public void startActivityForResult(Class<?> clz, int requestCode) {
        super.startActivityForResult(clz, requestCode);
    }

    @Override
    public void startActivityForResult(Class<?> clz, int requestCode, @NonNull Bundle bundle) {
        super.startActivityForResult(clz, requestCode, bundle);
    }

    @Override
    public Bundle getIntentBundle() {
        return super.getIntentBundle();
    }

    /**
     * 如果未注册[registerNetworkMonitoring] 会抛出空指针异常
     */
    @Override
    public Boolean isNetworkConnected() {
        return super.isNetworkConnected();
    }

    @Override
    public void showLoadDialog() {
        super.showLoadDialog();
    }

    @Override
    public void dismissLoadDialog() {
        super.dismissLoadDialog();
    }

    @Override
    public void showLongToast(String msg) {
        super.showLongToast(msg);
    }

    @Override
    public void showShortToast(String msg) {
        super.showShortToast(msg);
    }

}
