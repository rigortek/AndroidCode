package com.cw.childrenabc.ui.single

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cw.childrenabc.AppPreferences
import com.cw.childrenabc.R

//class SingleViewModel : ViewModel() {
class SingleViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    var fullBigLetterList: ArrayList<Int>
    var fullSmallLetterList: ArrayList<Int>
    var fullNumberList: ArrayList<Int>

    lateinit var unusedBigLetter: ArrayList<Int>
    lateinit var unusedSmallLetter: ArrayList<Int>
    lateinit var unusedNumber: ArrayList<Int>

    var usedNumber: ArrayList<Int> = ArrayList()
    var usedLetter: ArrayList<Int> = ArrayList()


    var nextPicture: Int = 0
    var testTotalCount = 0
    var testRightCount = 0

    init {
        fullBigLetterList = arrayListOf(
            R.drawable.letter_a, R.drawable.letter_b, R.drawable.letter_c,
            R.drawable.letter_d, R.drawable.letter_e, R.drawable.letter_f,
            R.drawable.letter_g, R.drawable.letter_h, R.drawable.letter_i,
            R.drawable.letter_j, R.drawable.letter_k, R.drawable.letter_l,
            R.drawable.letter_m, R.drawable.letter_n, R.drawable.letter_o,
            R.drawable.letter_p, R.drawable.letter_q, R.drawable.letter_r,
            R.drawable.letter_s, R.drawable.letter_t, R.drawable.letter_u,
            R.drawable.letter_v, R.drawable.letter_w, R.drawable.letter_x,
            R.drawable.letter_y, R.drawable.letter_z)

        unusedBigLetter = fullBigLetterList.clone() as ArrayList<Int>

        fullSmallLetterList = arrayListOf(
            R.drawable.letter_a_small, R.drawable.letter_b_small, R.drawable.letter_c_small,
            R.drawable.letter_d_small, R.drawable.letter_e_small, R.drawable.letter_f_small,
            R.drawable.letter_g_small, R.drawable.letter_h_small, R.drawable.letter_i_small,
            R.drawable.letter_j_small, R.drawable.letter_k_small, R.drawable.letter_l_small,
            R.drawable.letter_m_small, R.drawable.letter_n_small, R.drawable.letter_o_small,
            R.drawable.letter_p_small, R.drawable.letter_q_small, R.drawable.letter_r_small,
            R.drawable.letter_s_small, R.drawable.letter_t_small, R.drawable.letter_u_small,
            R.drawable.letter_v_small, R.drawable.letter_w_small, R.drawable.letter_x_small,
            R.drawable.letter_y_small, R.drawable.letter_z_small)

        unusedSmallLetter = fullSmallLetterList.clone() as ArrayList<Int>

        fullNumberList = arrayListOf(
            R.drawable.number_0, R.drawable.number_1, R.drawable.number_2,
            R.drawable.number_3, R.drawable.number_4, R.drawable.number_5,
            R.drawable.number_6, R.drawable.number_7, R.drawable.number_8, R.drawable.number_9
        )


        unusedNumber = fullNumberList.clone() as ArrayList<Int>
    }

    /*
    * -1 : unavailble, all done
    * > 0 : next random index
    *
    * */
    public fun generateNextIndex(): Int {
        val defaultIndex: Int = -1
        val appPreferences: AppPreferences = AppPreferences(context)
        val number_or_alphabet_value = appPreferences.get(AppPreferences.NUMBER_OR_LETTER, "number")

        if ("number".equals(number_or_alphabet_value)) {
            val unusedSize: Int = unusedNumber.size
            if (0 == unusedSize) {
                return defaultIndex
            } else {
                val random = (0..unusedSize - 1).random()
                val result = unusedNumber[random]
                unusedNumber.remove(result)
                return result
            }
        } else if ("letter".equals(number_or_alphabet_value)) {
            val big_or_small_alphabet_value = appPreferences.get(AppPreferences.BIG_OR_SMALL_LETTER, "small")
            if ("small".equals(big_or_small_alphabet_value)) {
                val unusedSize: Int = unusedSmallLetter.size
                if (0 == unusedSize) {
                    return defaultIndex
                } else {
                    val random = (0..unusedSize - 1).random()
                    val result = unusedSmallLetter[random]
                    unusedSmallLetter.remove(result)
                    return result
                }
            } else if ("big".equals(big_or_small_alphabet_value)) {
                val unusedSize: Int = unusedBigLetter.size
                if (0 == unusedSize) {
                    return defaultIndex
                } else {
                    val random = (0..unusedSize - 1).random()
                    val result = unusedBigLetter[random]
                    unusedBigLetter.remove(result)
                    return result
                }
            }
        }

        return defaultIndex;
    }

    public fun isIndexRecorded(index: Int): Boolean {
        val appPreferences: AppPreferences = AppPreferences(context)
        val number_or_alphabet_value = appPreferences.get(AppPreferences.NUMBER_OR_LETTER, "number")

        if ("number".equals(number_or_alphabet_value)) {
            return usedNumber.contains(index)
        } else if ("letter".equals(number_or_alphabet_value)) {
            // small and big share the same member
            return usedLetter.contains(index)
        }

        return false
    }

    public fun recordUsedIndex(index: Int) {
        val appPreferences: AppPreferences = AppPreferences(context)
        val number_or_alphabet_value = appPreferences.get(AppPreferences.NUMBER_OR_LETTER, "number")

        if ("letter".equals(number_or_alphabet_value)) {
            usedLetter.add(index)
        } else if ("number".equals(number_or_alphabet_value)) {
            usedNumber.add(index)
        } else {
            usedNumber.add(index)
        }
    }

    public fun totalTestCount(): Int {
        val appPreferences: AppPreferences = AppPreferences(context)
        val number_or_alphabet_value = appPreferences.get(AppPreferences.NUMBER_OR_LETTER, "number")


        if ("letter".equals(number_or_alphabet_value)) {
            return fullBigLetterList.size
        } else {
            return fullNumberList.size
        }
    }
}