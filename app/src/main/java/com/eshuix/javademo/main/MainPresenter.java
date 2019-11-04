package com.eshuix.javademo.main;

import com.eshuix.javademo.base.BasePresenter;

public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> implements MainContract.Presenter {

    @Override
    public MainContract.Model bindingModel() {
        return new MainModel();
    }
}
