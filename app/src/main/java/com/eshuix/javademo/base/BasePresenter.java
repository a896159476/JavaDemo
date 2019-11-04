package com.eshuix.javademo.base;


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
