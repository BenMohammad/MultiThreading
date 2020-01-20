package com.benmohammad.multithreading.exercises;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benmohammad.multithreading.R;
import com.benmohammad.multithreading.common.BaseFragment;

import java.util.concurrent.atomic.AtomicBoolean;

public class Exercise2Fragment extends BaseFragment {

    public static Fragment newInstance() {
        return new Exercise2Fragment();
    }

    private byte[] dummyData;
    private final AtomicBoolean mCountAbort = new AtomicBoolean(false);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dummyData = new byte[50 * 1000 * 1000];
        return inflater.inflate(R.layout.fragment_exercise2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        countScreenTime();

    }

    @Override
    public void onStop() {
        super.onStop();
        mCountAbort.set(true);
    }

    private void countScreenTime() {
        mCountAbort.set(false);

        new Thread(() -> {

            int screenTimeSeconds = 0;
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }

                if(mCountAbort.get()) {
                    return;
                }

                screenTimeSeconds++;
                Log.d("Exercise 2", screenTimeSeconds + "s");
            }


        }).start();
    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 2";
    }
}
