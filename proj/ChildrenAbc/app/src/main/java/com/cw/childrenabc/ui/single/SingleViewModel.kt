package com.cw.childrenabc.ui.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cw.childrenabc.R

class SingleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    var fullBigLetterList: ArrayList<Int>
    var fullSmallLetterList: ArrayList<Int>
    var fullNumberList: ArrayList<Int>

    lateinit var usedBigLetter: ArrayList<Int>
    lateinit var usedSmallLetter: ArrayList<Int>
    lateinit var usedNumber: ArrayList<Int>

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

        fullNumberList = arrayListOf(
            R.drawable.number_0, R.drawable.number_1, R.drawable.number_2,
            R.drawable.number_3, R.drawable.number_4, R.drawable.number_5,
            R.drawable.number_6, R.drawable.number_7, R.drawable.number_8, R.drawable.number_9
        )
    }
}