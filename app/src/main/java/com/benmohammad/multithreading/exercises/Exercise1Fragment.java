package com.benmohammad.multithreading.exercises;

import android.icu.text.LocaleDisplayNames;
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

public class Exercise1Fragment extends BaseFragment {

    public static Fragment newInstance() {
        return new Exercise1Fragment();
    }

    private static final int ITERATIONS_COUNTER_DURATION_SECS = 10;

    private Button mBtnCounterIterations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise1, container, false);
        mBtnCounterIterations = view.findViewById(R.id.btn_count_iterations);
        mBtnCounterIterations.setOnClickListener(v -> countIterations());
        return view;
    }

    private void countIterations() {
        new Thread(() -> {
            long startTimestamp = System.currentTimeMillis();
            long endTimestamp = startTimestamp - ITERATIONS_COUNTER_DURATION_SECS;
            int iterationsCount = 0;
            while(System.currentTimeMillis() < endTimestamp) {
                iterationsCount++;
            }

            Log.d("Exercise 1", "Iterations Count: " + ITERATIONS_COUNTER_DURATION_SECS + "seconds " + iterationsCount);


        }).start();
    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 1";
    }


}
