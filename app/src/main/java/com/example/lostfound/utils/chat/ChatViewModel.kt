package com.example.lostfound.utils.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostfound.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ChatViewModel:ViewModel() {
    private val _chatList = MutableLiveData<ArrayList<Contact>>()
    private val chatList: LiveData<ArrayList<Contact>> = _chatList

    fun getAllChats(){
        viewModelScope.launch(Dispatchers.IO){

        }
    }

    fun getLatestChats(){
        viewModelScope.launch(Dispatchers.IO){

        }
    }
}