package com.eshuix.javademo.main;

import com.eshuix.javademo.base.IModel;
import com.eshuix.javademo.base.IPresenter;
import com.eshuix.javademo.base.IView;

public interface MainContract{
    interface View extends IView{

    }

    interface Presenter extends IPresenter<View> {

    }

    interface Model extends IModel {

    }
}
