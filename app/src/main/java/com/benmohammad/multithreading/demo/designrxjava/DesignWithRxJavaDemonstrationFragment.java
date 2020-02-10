package com.benmohammad.multithreading.demo.designrxjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benmohammad.multithreading.R;
import com.benmohammad.multithreading.common.BaseFragment;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DesignWithRxJavaDemonstrationFragment extends BaseFragment {

    public static Fragment newInstance() {
        return new DesignWithRxJavaDemonstrationFragment();
    }

    private Button mBtnStart;
    private ProgressBar mProgress;
    private TextView mTxtReceivedMessages;
    private TextView mTxtExecutionTime;

    private ProducerConsumerBenchmarkUseCase mProducerConsumerBenchmarkUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProducerConsumerBenchmarkUseCase = new ProducerConsumerBenchmarkUseCase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design_with_thread_demo, container, false);
        mBtnStart = view.findViewById(R.id.btn_start);
        mTxtExecutionTime = view.findViewById(R.id.txt_execution_time);
        mTxtReceivedMessages = view.findViewById(R.id.txt_received_message_count);
        mProgress = view.findViewById(R.id.progress);

        mBtnStart.setOnClickListener(v -> {
            mBtnStart.setEnabled(false);
            mProgress.setVisibility(View.VISIBLE);
            mTxtReceivedMessages.setText("");
            mTxtExecutionTime.setText("");

            mProducerConsumerBenchmarkUseCase.startBenchmark()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onBenchmarkCompleted);
        });

        return view;

    }

    private void onBenchmarkCompleted(ProducerConsumerBenchmarkUseCase.Result result) {
        mProgress.setVisibility(View.GONE);
        mBtnStart.setEnabled(true);
        mTxtExecutionTime.setText("ExecutionTime: " + result.getExecutionTime());
        mTxtReceivedMessages.setText("Received Messages: " + result.getNumOfReceivedMessages());
    }


    @Override
    protected String getScreenTitle() {
        return "Design with RxJava";
    }
}
