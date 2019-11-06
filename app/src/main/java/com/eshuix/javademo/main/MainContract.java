package com.eshuix.javademo.main;

import com.eshuix.javademo.base.mvp.IModel;
import com.eshuix.javademo.base.mvp.IPresenter;
import com.eshuix.javademo.base.mvp.IView;

public interface MainContract{
    interface View extends IView{

    }

    interface Presenter extends IPresenter<View> {

    }

    interface Model extends IModel {

    }
}
