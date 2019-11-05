package com.eshuix.javademo.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public abstract class BaseFragment extends Fragment {

    private BaseActivity mActivity;

    private BaseActivity.OnMeasureSizeCallback onMeasureSizeCallback;
    private View[] views;

    public abstract int attachLayoutRes();

    /**
     * 初始化 View
     */
    public abstract void initView(View view);

    /**
     * 初始化数据
     */
    public abstract void initData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(attachLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    /**
     * 设置获取控件大小的回调
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setOnMeasureSizeCallback(BaseActivity.OnMeasureSizeCallback onMeasureSizeCallback, View... views) {
        this.onMeasureSizeCallback = onMeasureSizeCallback;
        this.views = views;
    }

    /**
     * fragment显示时获取控件大小
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && onMeasureSizeCallback != null) {
            for (View view : views) {
                getMeasureSize(view);
            }
        }
    }

    /**
     * 获取控件大小
     */
    private void getMeasureSize(final View view) {
        final ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                vto.removeOnGlobalLayoutListener(this);
                onMeasureSizeCallback.getMeasureSize(view);
            }
        });
    }

    /*
     * 获取宿主Activity
     */
    public BaseActivity getHoldingActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    /**
     * 重写startActivity
     *
     * @param clz 跳转页面的class
     */
    public void startActivity(Class<?> clz) {
        mActivity.startActivity(clz);
    }

    /**
     * 重写startActivity
     *
     * @param clz    跳转页面的class
     * @param bundle 携带的数据
     */
    public void startActivity(Class<?> clz, @NonNull Bundle bundle) {
        mActivity.startActivity(clz, bundle);
    }

    public void startActivityForResult(Class<?> clz, int requestCode) {
        mActivity.startActivityForResult(clz, requestCode);
    }

    public void startActivityForResult(Class<?> clz, int requestCode, @NonNull Bundle bundle) {
        mActivity.startActivityForResult(clz, requestCode, bundle);
    }

    public Bundle getIntentBundle() {
        return mActivity.getIntentBundle();
    }

    public Boolean isNetworkConnected() {
        return mActivity.isNetworkConnected();
    }

    /**
     * 显示加载Dialog
     */
    public void showLoadDialog() {
        mActivity.showLoadDialog();
    }

    /**
     * 关闭加载Dialog
     */
    public void dismissLoadDialog() {
        mActivity.dismissLoadDialog();
    }

    /**
     * 显示长Toast
     *
     * @param msg 显示的信息
     */
    public void showLongToast(String msg) {
        mActivity.showLongToast(msg);
    }

    /**
     * 显示短Toast
     *
     * @param msg 显示的信息
     */
    public void showShortToast(String msg) {
        mActivity.showShortToast(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }
}
