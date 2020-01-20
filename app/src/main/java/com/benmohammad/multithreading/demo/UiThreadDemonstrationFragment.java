package com.benmohammad.multithreading.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benmohammad.multithreading.R;
import com.benmohammad.multithreading.common.BaseFragment;

public class UiThreadDemonstrationFragment extends BaseFragment {

    public static Fragment newInstance() {
        return new UiThreadDemonstrationFragment();
    }

    private static final String TAG = "UiThreadDemo";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logThreadInfo("onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui_thread_demo, container, false);
        Button btnCallBack = view.findViewById(R.id.callBackCheck);
        btnCallBack.setOnClickListener(v -> logThreadInfo("Button Callback!!"));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logThreadInfo("onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logThreadInfo("onDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();
        logThreadInfo("onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        logThreadInfo("onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        logThreadInfo("onResume");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logThreadInfo("onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        logThreadInfo("onStart");
    }

    private void logThreadInfo(String s) {
        Log.d(TAG, "event\n" +
                        s
                        + ": thread name: " + Thread.currentThread().getName()
                        + "; thread ID: " + Thread.currentThread().getId());
    }

    @Override
    protected String getScreenTitle() {
        return "Ui Thread Demo";
    }
}
