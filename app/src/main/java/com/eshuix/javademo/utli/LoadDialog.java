package com.eshuix.javademo.utli;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eshuix.javademo.R;

public class LoadDialog extends DialogFragment {

    private static class LoadDialogHolder {
        private static final LoadDialog INSTANCE = new LoadDialog();
    }
    private LoadDialog (){}
    public static LoadDialog getInstance() {
        return LoadDialog.LoadDialogHolder.INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_progress_bar,container,false);
    }
}
