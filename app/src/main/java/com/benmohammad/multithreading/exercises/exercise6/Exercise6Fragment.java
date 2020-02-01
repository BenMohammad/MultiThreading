package com.benmohammad.multithreading.exercises.exercise6;

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

import java.math.BigInteger;

public class Exercise6Fragment extends BaseFragment implements ComputeFactorialUseCase.Listener {

    public static Fragment newInstance() {
        return new Exercise6Fragment();
    }


    private final static int MAX_SECONDS_MAX = 1000;
    private EditText mEdtTimeout, mEdtArgument;
    private Button mBtnStartWork;
    private TextView mTxtResult;

    private ComputeFactorialUseCase mComputeFactorialUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComputeFactorialUseCase = new ComputeFactorialUseCase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise6, container, false);
        mEdtArgument = view.findViewById(R.id.edt_argument);
        mEdtTimeout = view.findViewById(R.id.edt_timeout);
        mBtnStartWork = view.findViewById(R.id.btn_compute);
        mTxtResult = view.findViewById(R.id.txt_result);

        mBtnStartWork.setOnClickListener(v -> {
            if(mEdtArgument.getText().toString().isEmpty()) {
                return;
            }

            mBtnStartWork.setEnabled(false);
            mTxtResult.setText("");

            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mBtnStartWork.getWindowToken(), 0);

            int argument = Integer .valueOf(mEdtArgument.getText().toString());

            mComputeFactorialUseCase.computeFactoralAndNotify(argument, getTimeout());


        });

        return view;
    }

    private int getTimeout() {
        int timeout;
        if(mEdtTimeout.getText().toString().isEmpty()) {
            timeout = MAX_SECONDS_MAX;
        } else {
            timeout = Integer.valueOf(mEdtTimeout.getText().toString());
            if(timeout > MAX_SECONDS_MAX) {
                timeout = MAX_SECONDS_MAX;
            }
        }
        return timeout;

    }



    @Override
    public void onStop() {
        super.onStop();
        mComputeFactorialUseCase.unregisterListner(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        mComputeFactorialUseCase.registerListener(this);
    }


    @Override
    protected String getScreenTitle() {
        return "Exercise 6";
    }


    @Override
    public void onFactorialComputed(BigInteger result) {
        mTxtResult.setText(result.toString());
        mBtnStartWork.setEnabled(true);
    }

    @Override
    public void onFactorialComputationTimedout() {
        mTxtResult.setText("Timedout!!");
        mBtnStartWork.setEnabled(true);
    }

    @Override
    public void onFactorialComputationAborted() {
        mTxtResult.setText("Aborted!!");
        mBtnStartWork.setEnabled(true);
    }
}
