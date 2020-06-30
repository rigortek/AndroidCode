package com.cw.childrenabc.ui.multi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
}
