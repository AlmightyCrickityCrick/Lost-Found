package com.example.lostfound.utils.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lostfound.utils.ApolloClientService
import kotlinx.coroutines.*


class OTPViewModel:ViewModel(){
     private val _isSuccess = MutableLiveData<Boolean>()
     val isSuccess: LiveData<Boolean> = _isSuccess

    private val _OTPId = MutableLiveData<Int>()
    val OTPId: LiveData<Int> = _OTPId
    fun sendOTP(email:String){
        runBlocking {
            val job = GlobalScope.async {
                ApolloClientService.sendOTP(email)
            }
            val result = job.await()
            if(result!=null) _OTPId.value = result
    }
    }

    fun getResult(id:String, code:String){
        runBlocking {
            val job = GlobalScope.async {
                ApolloClientService.getOTP(id.toInt(), code)
            }
            val result = job.await()
            _isSuccess.value = result=="Success"
        }
    }

}