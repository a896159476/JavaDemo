package com.eshuix.javademo.base;


import com.eshuix.javademo.base.mvp.IModel;
import com.eshuix.javademo.base.mvp.IPresenter;
import com.eshuix.javademo.base.mvp.IView;

public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter<V> {

    public V mView;
    public M mModel;

    public abstract M bindingModel();

    @Override
    public void bindingView(V view) {
        mView = view;
        mModel = bindingModel();
    }

    @Override
    public void onDestroy() {
        mModel.onDestroy();
        mModel = null;
        mView = null;
    }
}
