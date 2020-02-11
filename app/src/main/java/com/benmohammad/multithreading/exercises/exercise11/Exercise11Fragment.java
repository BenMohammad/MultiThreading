package com.benmohammad.multithreading.exercises.exercise11;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benmohammad.multithreading.R;
import com.benmohammad.multithreading.common.BaseFragment;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.math.BigInteger;

public class Exercise11Fragment extends BaseFragment implements ComputeFactorialUseCase.Listener {

    public static Fragment newInstance() {
        return new Exercise11Fragment();
    }

    private final static int MAX_TIMEOUT_MS = 1000;
    private EditText mEdtArgument;
    private EditText mEdtTimeout;
    private Button mBtnStartWork;
    private TextView mTxtResult;

    private ComputeFactorialUseCase mComputeFactorialUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComputeFactorialUseCase = new ComputeFactorialUseCase(
                new BackgroundThreadPoster(),
                new UiThreadPoster()
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise11, container, false);
        mEdtArgument = view.findViewById(R.id.edt_argument);
        mEdtTimeout = view.findViewById(R.id.edt_timeout);
        mTxtResult = view.findViewById(R.id.txt_result);
        mBtnStartWork = view.findViewById(R.id.btn_compute);
        mBtnStartWork.setOnClickListener(v -> {
            if(mEdtArgument.getText().toString().isEmpty()) {
                return;
            }
            mBtnStartWork.setEnabled(false);
            mTxtResult.setText("");
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mBtnStartWork.getWindowToken(), 0);

            int argument = Integer.valueOf(mEdtArgument.getText().toString());

            mComputeFactorialUseCase.computeFactorialAndNotify(argument, getTimeout());

        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mComputeFactorialUseCase.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mComputeFactorialUseCase.unregisterListner(this);
    }

    private int getTimeout() {
        int timeout;
        if(mEdtTimeout.getText().toString().isEmpty()) {
            timeout = MAX_TIMEOUT_MS;
        } else {
            timeout = Integer.valueOf(mEdtTimeout.getText().toString());
            if(timeout > MAX_TIMEOUT_MS) {
                timeout = MAX_TIMEOUT_MS;
            }
        }
        return timeout;
    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 11";
    }

    @Override
    public void onFactorialComputed(BigInteger result) {
        mBtnStartWork.setEnabled(true);
        mTxtResult.setText(result.toString());
    }

    @Override
    public void onFactorialTimedOut() {
        mBtnStartWork.setEnabled(true);
        mTxtResult.setText("Timeout!!");

    }
}
