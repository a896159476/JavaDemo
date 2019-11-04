package com.eshuix.javademo.main;

import android.os.Build;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.eshuix.javademo.R;
import com.eshuix.javademo.base.BaseMvpActivity;

public class MainActivity extends BaseMvpActivity<MainContract.View,MainContract.Presenter> implements MainContract.View{

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected MainContract.Presenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        ImageView ancient = findViewById(R.id.ancient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOnMeasureSizeCallback(new OnMeasureSizeCallback() {
                @Override
                public void getMeasureSize(View view, Size size) {
                    switch (view.getId()){
                        case R.id.ancient:
                        case R.id.toolbar:
                            showLongToast("宽度：" + size.getWidth() + "高度：" + size.getHeight());
                            break;
                    }
                }
            });
            viewPost(ancient).viewPost(toolbar);
        }
    }

}
