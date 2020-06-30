package com.cw.childrenabc.ui.single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cw.childrenabc.R

class SingleFragment : Fragment() {

    private lateinit var singleViewModel: SingleViewModel

    private lateinit var textViewCurCharactor: TextView
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
        textViewCurCharactor = root.findViewById(R.id.text_cur_charactor)
        singleViewModel.text.observe(viewLifecycleOwner, Observer {
            textViewCurCharactor.text = it
        })

        radioButtonWrong = root.findViewById(R.id.check_wrong)
        radioButtonRight = root.findViewById(R.id.check_right)

        return root
    }


}
