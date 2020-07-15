package com.cw.childrenabc.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cw.childrenabc.AppPreferences
import com.cw.childrenabc.R

class SettingFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel

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
        val number_or_alphabet = root.findViewById(R.id.number_or_alphabet) as RadioGroup

        val number_or_alphabet_value = appPreferences.get(AppPreferences.NUMBER_OR_ALPHABET, "number")
        number_or_alphabet.clearCheck()
        if ("number".equals(number_or_alphabet_value)) {
            number_or_alphabet.check(R.id.number)
//            val number = root.findViewById(R.id.number) as RadioButton
//            number.setChecked(true)
        } else if ("alphabet".equals(number_or_alphabet_value)) {
            number_or_alphabet.check(R.id.alphabet)
//            val alphabet = root.findViewById(R.id.alphabet) as RadioButton
//            alphabet.setChecked(true)
        }

        number_or_alphabet.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {

                if (p1 == R.id.number) {
                    appPreferences.save(AppPreferences.NUMBER_OR_ALPHABET, "number")
                } else if (p1 == R.id.alphabet) {
                    appPreferences.save(AppPreferences.NUMBER_OR_ALPHABET, "alphabet")
                }
            }
        })

        val big_or_small_alphabet = root.findViewById(R.id.big_or_small_alphabet) as RadioGroup
        big_or_small_alphabet.clearCheck()
        val big_or_small_alphabet_value = appPreferences.get(AppPreferences.BIG_OR_SMALL_ALPHABET, "small")
        if ("small".equals(big_or_small_alphabet_value)) {
            number_or_alphabet.check(R.id.small_alphabet)
        } else if ("big".equals(big_or_small_alphabet_value)) {
            number_or_alphabet.check(R.id.big_alphabet)
        }

        big_or_small_alphabet.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                if (p1 == R.id.small_alphabet) {
                    appPreferences.save(AppPreferences.BIG_OR_SMALL_ALPHABET, "small")
                } else if (p1 == R.id.big_alphabet) {
                    appPreferences.save(AppPreferences.BIG_OR_SMALL_ALPHABET, "big")
                }
            }
        })

        return root
    }
}
