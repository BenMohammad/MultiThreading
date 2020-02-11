package com.benmohammad.multithreading.exercises.exercise12;

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

public class Exercise12Fragment extends BaseFragment implements ProducerConsumerBenchmarkUseCase.Listener {

    public static Fragment newInstance() {
        return new Exercise12Fragment();
    }

    private TextView mTxtExecutionTime;
    private TextView mTxtReceivedMessages;
    private ProgressBar mProgress;
    private Button mBtnStartWork;

    private ProducerConsumerBenchmarkUseCase producerConsumerBenchmarkUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        producerConsumerBenchmarkUseCase = new ProducerConsumerBenchmarkUseCase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise12, container, false);
        mTxtReceivedMessages = view.findViewById(R.id.txt_received_message_count);
        mTxtExecutionTime = view.findViewById(R.id.txt_execution_time);
        mProgress = view.findViewById(R.id.progress);
        mBtnStartWork = view.findViewById(R.id.btn_start);

        mBtnStartWork.setOnClickListener(v -> {
            mBtnStartWork.setEnabled(false);
            mTxtExecutionTime.setText("");
            mTxtReceivedMessages.setText("");
            mProgress.setVisibility(View.VISIBLE);

            producerConsumerBenchmarkUseCase.startBenchmarkAndNotify();
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        producerConsumerBenchmarkUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        producerConsumerBenchmarkUseCase.unregisterListner(this);
    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 12";
    }

    @Override
    public void onBenchmarkCompleted(ProducerConsumerBenchmarkUseCase.Result result) {
        mProgress.setVisibility(View.GONE);
        mBtnStartWork.setEnabled(true);
        mTxtReceivedMessages.setText("Received Messages: " + result.getNumOfReceivedMessages());
        mTxtExecutionTime.setText("Execution Time: " + result.getExecutionTime());
    }
}
