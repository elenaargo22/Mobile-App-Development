package com.example.layoutsandactions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class User() : ViewModel(){
    val firstName = MutableLiveData<String>("")
    val lastName = MutableLiveData<String>("")
}
