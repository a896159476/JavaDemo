package com.eshuix.javademo.utli;

import android.app.Application;

import com.eshuix.javademo.http.HttpHelper;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpHelper.getInstance(this).getBuilder();
    }
}
