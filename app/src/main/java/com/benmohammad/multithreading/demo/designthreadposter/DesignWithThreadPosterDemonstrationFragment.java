package com.benmohammad.multithreading.demo.designthreadposter;

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

public class DesignWithThreadPosterDemonstrationFragment extends BaseFragment implements ProducerConsumerBenchmarkUseCase.Listener {

    public static Fragment newInstance() {
        return new DesignWithThreadPosterDemonstrationFragment();
    }

    private Button mBtnStart;
    private ProgressBar mProgress;
    private TextView mTxtReeivedMessagesCount;
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
        mProgress = view.findViewById(R.id.progress);
        mTxtExecutionTime = view.findViewById(R.id.txt_execution_time);
        mTxtReeivedMessagesCount = view.findViewById(R.id.txt_received_message_count);

        mBtnStart.setOnClickListener(v -> {
            mBtnStart.setEnabled(false);
            mTxtReeivedMessagesCount.setText("");
            mTxtExecutionTime.setText("");
            mProgress.setVisibility(View.VISIBLE);

            mProducerConsumerBenchmarkUseCase.startBenchmarkAndNotify();
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        mProducerConsumerBenchmarkUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mProducerConsumerBenchmarkUseCase.unregisterListner(this);
    }

    @Override
    protected String getScreenTitle() {
        return "Design with ThreadPoster";
    }

    @Override
    public void onBenchmarkCompleted(ProducerConsumerBenchmarkUseCase.Result result) {
        mProgress.setVisibility(View.GONE);
        mTxtReeivedMessagesCount.setText("Received Messages: " + result.getNumOfReceivedMessages());
        mTxtExecutionTime.setText("Execution Time: " + result.getExecutionTime());
        mBtnStart.setEnabled(true);
    }
}
