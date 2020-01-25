package com.benmohammad.multithreading.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benmohammad.multithreading.R;
import com.benmohammad.multithreading.common.BaseFragment;

public class AtomicityDemonstrationFragment extends BaseFragment {

    private final int COUNT_UP_TO = 1000;
    private final int NUMBER_OF_COUNT_THREADS = 1000;

    public static Fragment newInstance() {
        return new AtomicityDemonstrationFragment();
    }

    private Button mBtnStartCount;
    private TextView mTxtFinalCount;

    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private volatile int mCount;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atomicity_demo, container, false);
        mTxtFinalCount = view.findViewById(R.id.txt_final_count);
        mBtnStartCount = view.findViewById(R.id.btn_start_count);

        mBtnStartCount.setOnClickListener(  v -> startCount());


        return view;
    }

    private void startCount() {
        mCount = 0;
        mTxtFinalCount.setText("");
        mBtnStartCount.setEnabled(false);

        for(int i = 0; i < NUMBER_OF_COUNT_THREADS; i++) {
            startCountThread();
        }

        mUiHandler.postDelayed(() -> {
            mTxtFinalCount.setText(String.valueOf(mCount));
            mBtnStartCount.setEnabled(true);
        }, NUMBER_OF_COUNT_THREADS * 20);

    }

    private void startCountThread() {
        new Thread(() -> {
            for(int i =0; i < COUNT_UP_TO; i++) {
                mCount++;
            }
        }).start();
    }

    @Override
    protected String getScreenTitle() {
        return "Atomicity Demo";
    }
}
