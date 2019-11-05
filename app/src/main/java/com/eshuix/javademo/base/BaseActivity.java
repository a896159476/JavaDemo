package com.eshuix.javademo.base;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.eshuix.javademo.utli.NetworkCallbackImpl;
import com.eshuix.javademo.utli.LoadDialog;

public abstract class BaseActivity extends AppCompatActivity {

    private LoadDialog loadDialog;

    private NetworkCallbackImpl networkCallback;
    private ConnectivityManager connMgr;

    private OnMeasureSizeCallback onMeasureSizeCallback;
    private View[] views;

    /**
     * 布局文件id
     */
    protected abstract int attachLayoutRes();

    /**
     * 初始化 View
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //防止截屏攻击风险
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        //仿系统自带短信界面 可以完全漂浮在软键盘之上
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());

        initView();
        initData();
        initTipView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            registerNetworkMonitoring();
        }

    }

    /**
     * 初始化提示控件
     */
    private void initTipView() {
        //加载Dialog
        loadDialog = LoadDialog.getInstance();
        loadDialog.setCancelable(false);
    }

    /**
     * 获取控件大小接口
     */
    public interface OnMeasureSizeCallback {
        void getMeasureSize(View view);
    }

    /**
     * 设置获取控件大小的回调
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setOnMeasureSizeCallback(OnMeasureSizeCallback onMeasureSizeCallback, View... views) {
        this.onMeasureSizeCallback = onMeasureSizeCallback;
        this.views = views;
    }

    /**
     * 获取焦点时调用获取控件大小的回调
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && onMeasureSizeCallback != null) {
            for (View view : views) {
                onMeasureSizeCallback.getMeasureSize(view);
            }
        }
    }

    /**
     * 重写startActivity
     *
     * @param clz 跳转页面的class
     */
    public void startActivity(Class<?> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
        enterTransitionAnim();
    }

    /**
     * 重写startActivity
     *
     * @param clz    跳转页面的class
     * @param bundle 携带的数据
     */
    public void startActivity(Class<?> clz, @NonNull Bundle bundle) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
        enterTransitionAnim();
    }

    public void startActivityForResult(Class<?> clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode);
        enterTransitionAnim();
    }

    public void startActivityForResult(Class<?> clz, int requestCode, @NonNull Bundle bundle) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode, bundle);
        enterTransitionAnim();
    }

    public Bundle getIntentBundle() {
        return getIntent().getExtras();
    }

    /**
     * 进入Activity过渡动画
     */
    private void enterTransitionAnim() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 退出Activity过渡动画
     */
    private void quitTransitionAnim() {
        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
    }

    /**
     * 检测网络是否连接
     * 如果未注册[registerNetworkMonitoring] 会抛出空指针异常
     */
    public Boolean isNetworkConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (networkCallback != null) {
                return networkCallback.isNetworkConnected();
            }
        } else {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager != null) {
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
        }
        throw new NullPointerException();
    }

    /**
     * 注册网络监听
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void registerNetworkMonitoring() {
        networkCallback = NetworkCallbackImpl.getInstance();
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            //需要<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connMgr.registerDefaultNetworkCallback(networkCallback);
            } else {
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                NetworkRequest request = builder.build();
                connMgr.registerNetworkCallback(request, networkCallback);
            }
        }
    }

    /**
     * 注销网络监听
     */
    private void unregisterNetworkMonitoring() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (connMgr != null) {
                connMgr.unregisterNetworkCallback(networkCallback);
            }
        }
    }

    /**
     * 显示加载Dialog
     */
    public void showLoadDialog() {
        loadDialog.show(getSupportFragmentManager(), this.getPackageName());
    }

    /**
     * 关闭加载Dialog
     */
    public void dismissLoadDialog() {
        loadDialog.dismiss();
    }

    /**
     * 显示长Toast
     *
     * @param msg 显示的信息
     */
    public void showLongToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示短Toast
     *
     * @param msg 显示的信息
     */
    public void showShortToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Fragment 逐个出栈
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }

        quitTransitionAnim();
    }

    @Override
    public void finish() {
        super.finish();
        quitTransitionAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkMonitoring();
    }

    /**
     * 沉浸状态栏
     * 重写onWindowFocusChanged方法实现沉浸模式
     */
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);//设置虚拟按键透明
//            getWindow().setStatusBarColor(Color.TRANSPARENT);//设置状态栏透明
//        } else if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }

}
