package com.eshuix.javademo.base.mvp;

import android.os.Bundle;

import androidx.annotation.NonNull;

public interface IView {

    void showLoadDialog();
    void dismissLoadDialog();
    void showLongToast(String msg);
    void showShortToast(String msg);

    Boolean isNetworkConnected();

    void startActivity(Class<?> clz);
    void startActivity(Class<?> clz, @NonNull Bundle bundle);
    void startActivityForResult(Class<?> clz, int requestCode);
    void startActivityForResult(Class<?> clz, int requestCode, @NonNull Bundle bundle);
    Bundle getIntentBundle();
}
