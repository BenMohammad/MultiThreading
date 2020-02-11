package com.benmohammad.multithreading.demo.designcoroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.benmohammad.multithreading.R
import com.benmohammad.multithreading.common.BaseFragment

class DesignWithCoroutinesDemoFragment : BaseFragment(), ProducerConsumerBenchmarkUseCase.Listener {

    private lateinit var btnStart : Button
    private lateinit var progress : ProgressBar
    private lateinit var txtReceivedMessagesCount : TextView
    private lateinit var txtExecutionTime : TextView

    private lateinit var producerConsumerBenchmarkUseCase: ProducerConsumerBenchmarkUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        producerConsumerBenchmarkUseCase = ProducerConsumerBenchmarkUseCase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_design_with_coroutines, container, false);
        progress = view.findViewById(R.id.progress)
        txtReceivedMessagesCount = view.findViewById(R.id.txt_received_message_count)
        txtExecutionTime = view.findViewById(R.id.txt_execution_time)
        btnStart = view.findViewById(R.id.btn_start)

        btnStart.setOnClickListener {
            btnStart.isEnabled = false
            txtExecutionTime.text = ""
            txtReceivedMessagesCount.text = ""
            progress.visibility = View.VISIBLE

            producerConsumerBenchmarkUseCase.startBenchmarkAndNotify()
        }

        return view
    }


    override fun onStart() {
        super.onStart()
        producerConsumerBenchmarkUseCase.registerListener(this)
    }


    override fun onStop() {
        super.onStop()
        producerConsumerBenchmarkUseCase.unregisterListner(this)
    }


    override fun onBenchmarkCompleted(result: ProducerConsumerBenchmarkUseCase.Result) {
        progress.visibility = View.GONE
        btnStart.isEnabled = true
        txtReceivedMessagesCount.text = "Received Messages:  ${result.numOfreceivedMessages}"
        txtExecutionTime.text = "ExecutionTime:  ${result.executionTime}"
    }


    override fun getScreenTitle(): String = "Design with Coroutines"


    companion object {
        fun newInstance() : Fragment {
            return DesignWithCoroutinesDemoFragment()
        }
    }
}