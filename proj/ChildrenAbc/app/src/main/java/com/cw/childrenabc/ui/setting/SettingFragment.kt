package com.cw.childrenabc.ui.setting

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.cw.childrenabc.AppPreferences
import com.cw.childrenabc.Constants
import com.cw.childrenabc.R

class SettingFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel

    private lateinit var number_or_letter: RadioGroup
    private lateinit var big_or_small_letter: RadioGroup

    private lateinit var letter: RadioButton
    private lateinit var number: RadioButton

    private lateinit var small_letter: RadioButton
    private lateinit var big_letter: RadioButton

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_setting, container, false)
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        settingViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val appPreferences: AppPreferences = AppPreferences(activity)
        number_or_letter = root.findViewById(R.id.number_or_letter) as RadioGroup

        letter = root.findViewById(R.id.letter) as RadioButton
        number = root.findViewById(R.id.number) as RadioButton

        val number_or_alphabet_value = appPreferences.get(AppPreferences.NUMBER_OR_LETTER, "number")
        number_or_letter.clearCheck()
        if ("number".equals(number_or_alphabet_value)) {
//            number_or_letter.check(R.id.number)
            number.setChecked(true)
        } else if ("letter".equals(number_or_alphabet_value)) {
//            number_or_letter.check(R.id.letter)
            letter.setChecked(true)
        }

        number_or_letter.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {

                if (p1 == R.id.number) {
                    appPreferences.save(AppPreferences.NUMBER_OR_LETTER, "number")
                } else if (p1 == R.id.letter) {
                    appPreferences.save(AppPreferences.NUMBER_OR_LETTER, "letter")
                }
            }
        })

        big_or_small_letter = root.findViewById(R.id.big_or_small_letter) as RadioGroup
        big_letter = root.findViewById(R.id.big_letter) as RadioButton
        small_letter = root.findViewById(R.id.small_letter) as RadioButton

        big_or_small_letter.clearCheck()
        val big_or_small_alphabet_value = appPreferences.get(AppPreferences.BIG_OR_SMALL_LETTER, "small")
        if ("small".equals(big_or_small_alphabet_value)) {
//            number_or_letter.check(R.id.small_letter)
            small_letter.isChecked = true
        } else if ("big".equals(big_or_small_alphabet_value)) {
//            number_or_letter.check(R.id.big_letter)
            big_letter.isChecked = true
        }

        big_or_small_letter.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                if (p1 == R.id.small_letter) {
                    appPreferences.save(AppPreferences.BIG_OR_SMALL_LETTER, "small")
                } else if (p1 == R.id.big_letter) {
                    appPreferences.save(AppPreferences.BIG_OR_SMALL_LETTER, "big")
                }
            }
        })

        return root
    }

    // onCreate early than onCreateView
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
