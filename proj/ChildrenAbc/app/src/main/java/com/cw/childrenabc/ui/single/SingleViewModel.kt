package com.cw.childrenabc.ui.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SingleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    var fullCharactorList: ArrayList<String>
    lateinit var remainingList: ArrayList<String>

    init {
        fullCharactorList = arrayListOf(
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "G",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z")
    }
}