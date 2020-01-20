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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class CustomHandlerDemonstrationFragment extends BaseFragment {


    private static final int SECONDS_TO_COUNT = 10;


    public static Fragment newInstance() {
        return new CustomHandlerDemonstrationFragment();
    }

    private Button mBtnSendJob;
    private CustomHandler mCustomHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_looper_demo, container, false);
        mBtnSendJob = view.findViewById(R.id.btn_send_job);
        mBtnSendJob.setOnClickListener(v -> sendJob());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mCustomHandler = new CustomHandler();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCustomHandler.stop();
    }

    private void sendJob() {
        mCustomHandler.post(() -> {
            for(int i = 0; i < SECONDS_TO_COUNT; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }

                Log.d("CustomHandler", "Iteration: " + i);

            }
        });
    }

    @Override
    protected String getScreenTitle() {
        return "Custom Handler";
    }

    private class CustomHandler {
        private final Runnable POISON = () -> {

        };

        private final BlockingQueue<Runnable> mQueue = new LinkedBlockingDeque<>();

        public CustomHandler() {
            initWorkThread();
        }

        private void initWorkThread() {
            new Thread(() -> {
                Log.d("CustomHandler", "worker (looper) thread initialized");
                while(true) {
                    Runnable runnable;
                    try {
                        runnable = mQueue.take();
                    }catch (InterruptedException e) {
                        return;
                    }

                    runnable.run();
                }
            }).start();
        }

        public void stop() {
            Log.d("CustomHandler", "Injecting poison data in queue");
        }

        public void post(Runnable job) {
            mQueue.add(job);
        }
    }

}
