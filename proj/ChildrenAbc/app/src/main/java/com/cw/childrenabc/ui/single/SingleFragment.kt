package com.cw.childrenabc.ui.single

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cw.childrenabc.Constants
import com.cw.childrenabc.R
import com.cw.childrenabc.ToastUtil

class SingleFragment : Fragment() {

    private lateinit var singleViewModel: SingleViewModel

//    private lateinit var textViewCurCharactor: TextView
    private lateinit var radioButtonWrong: RadioButton
    private lateinit var radioButtonRight: RadioButton

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        singleViewModel =
                ViewModelProviders.of(this).get(SingleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_single, container, false)
//        textViewCurCharactor = root.findViewById(R.id.text_cur_charactor)
//        singleViewModel.text.observe(viewLifecycleOwner, Observer {
//            textViewCurCharactor.text = it
//        })


        radioButtonWrong = root.findViewById(R.id.check_wrong)
        radioButtonWrong.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                singleViewModel.testTotalCount += 1

                activity?.let { ToastUtil.showLong(it, "噢噢，选错了，请加油") }
            }

        })
        radioButtonRight = root.findViewById(R.id.check_right)
        radioButtonRight.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                singleViewModel.testRightCount += 1
                singleViewModel.testTotalCount += 1

                activity?.let { ToastUtil.showLong(it, "哈哈，选对了，你真棒") }
            }
        })

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(Constants.TAG, "onCreate: ")
    }

    override fun onStart() {
        super.onStart()
        Log.d(Constants.TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(Constants.TAG, "onResume: ")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(Constants.TAG, "onAttach: ")
    }

    override fun onDetach() {

        Log.d(Constants.TAG, "onDetach: ")
        super.onDetach()
    }

    override fun onPause() {

        Log.d(Constants.TAG, "onPause: ")
        super.onPause()
    }

    override fun onStop() {

        Log.d(Constants.TAG, "onStop: ")
        super.onStop()
    }

    override fun onDestroyView() {

        Log.d(Constants.TAG, "onDestroyView: ")
        super.onDestroyView()
    }

    override fun onDestroy() {

        Log.d(Constants.TAG, "onDestroy: ")
        super.onDestroy()
    }
}
