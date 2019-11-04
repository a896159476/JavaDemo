package com.eshuix.javademo.base;

public interface IPresenter<V extends IView> {

    void bindingView(V view);

    void onDestroy();
}
