package com.eshuix.javademo.base.mvp;

public interface IPresenter<V extends IView> {

    void bindingView(V view);

    void onDestroy();
}
