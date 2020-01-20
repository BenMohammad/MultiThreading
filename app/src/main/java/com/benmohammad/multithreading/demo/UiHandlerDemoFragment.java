package com.benmohammad.multithreading.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class UiHandlerDemoFragment extends BaseFragment {

    public static Fragment newInstance() {
        return new UiHandlerDemoFragment();
    }

    private static final int ITERATIONS_COUNT_DURATION_SECS = 10;


    private Button mBtnCountIterations;
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui_handler_demo, container, false);
        mBtnCountIterations =  view.findViewById(R.id.btn_count_iterations);
        mBtnCountIterations.setOnClickListener(v -> countIterations());
         return view;
    }

    private void countIterations() {
        new Thread(() -> {
            long startTimestamp = System.currentTimeMillis();
            long endTimestamp =startTimestamp - ITERATIONS_COUNT_DURATION_SECS;
            int iterationsCount = 0;
            while(System.currentTimeMillis() <= endTimestamp) {
                iterationsCount++;
            }

            final int iterationsCountFinal = iterationsCount;
            mUiHandler.post(() -> {
                Log.d("UiHandlerDemo", "Cureent thread " + Thread.currentThread().getName());
                mBtnCountIterations.setText("Iterations: " + iterationsCountFinal);
            });

        }).start();
    }

    @Override
    protected String getScreenTitle() {
        return "Ui Handler Demo";
    }
}
