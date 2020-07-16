package com.cw.childrenabc.ui.multi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cw.childrenabc.Constants
import com.cw.childrenabc.R

class MultiFragment : Fragment() {

    private lateinit var multiViewModel: MultiViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        multiViewModel =
                ViewModelProviders.of(this).get(MultiViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_multi, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        multiViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
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
