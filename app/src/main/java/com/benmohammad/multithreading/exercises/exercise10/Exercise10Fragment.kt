package com.benmohammad.multithreading.exercises.exercise10

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.benmohammad.multithreading.R
import com.benmohammad.multithreading.common.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.math.BigInteger

class Exercise10Fragment : BaseFragment() {

    private lateinit var edtArgument : EditText
    private lateinit var edtTimeout : EditText
    private lateinit var txtResult : TextView
    private lateinit var btnStart : Button
    private lateinit var computeFactorialUseCase: ComputeFactorialUseCase

    private var job : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        computeFactorialUseCase = ComputeFactorialUseCase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_exercise10, container, false)
        view.apply {
            btnStart = findViewById(R.id.btn_compute)
            txtResult = findViewById(R.id.txt_result)
            edtArgument = findViewById(R.id.edt_argument)
            edtTimeout = findViewById(R.id.edt_timeout)

            btnStart.setOnClickListener {
                if(edtArgument.text.toString().isEmpty()) {
                    return@setOnClickListener
                }
                txtResult.text = ""
                btnStart.isEnabled = false
                val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(btnStart.applicationWindowToken, 0)

                val argument = Integer.valueOf(edtArgument.text.toString())

                job = CoroutineScope(Dispatchers.Main).launch {
                    when(val result = computeFactorialUseCase.computeFactorial(argument, getTimeout())) {
                        is ComputeFactorialUseCase.Result.Success -> onFactorialComputationSuccess(result.result)
                        is ComputeFactorialUseCase.Result.Timeout -> onFactorialComputationTimeout()
                    }
                }
            }
        }

        return view

    }

    override fun onStop() {
        super.onStop()
        job?.apply { cancel() }
    }

    fun onFactorialComputationSuccess(result: BigInteger) {
        txtResult.text = result.toString()
        btnStart.isEnabled = true
    }

    fun onFactorialComputationTimeout() {
        txtResult.text = "Timeout"
        btnStart.isEnabled = true
    }

    private fun getTimeout(): Int {
        var timeout: Int
        if (edtTimeout.text.toString().isEmpty()) {
            timeout = MAX_TIMEOUT_MS
        } else {
            timeout = Integer.valueOf(edtTimeout.text.toString())
            if(timeout > MAX_TIMEOUT_MS) {
                timeout = MAX_TIMEOUT_MS
            }
        }

        return timeout
    }

    override fun getScreenTitle(): String = "Exercise 10"

    companion object {
        private const val MAX_TIMEOUT_MS = 1000

        fun newInstance() : Fragment {
            return Exercise10Fragment()
        }
    }



}