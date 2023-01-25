package com.example.lostfound.utils.chat

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lostfound.data.model.Contact
import com.example.lostfound.utils.ApolloClientService
import com.example.lostfound.utils.crypto.CryptographyService
import com.example.lostfound.utils.login.LoggedInUserView
import kotlinx.coroutines.*
import java.util.*

data class ChatFetchResult(val success: ArrayList<Contact>? = null,
                           val error:Int? = null)

class ChatListViewModel :ViewModel(){
    private val _chatList = MutableLiveData<ChatFetchResult>()
    val chatList:LiveData<ChatFetchResult> = _chatList
    private var key :String?=null

    fun getAllChats(myId: String){
        runBlocking {
            val job = GlobalScope.async {
                ApolloClientService.getAllChats(myId)
            }

            val result = job.await()
            if(!result.isEmpty()){
                _chatList.value = ChatFetchResult(success = result)
            }
        }
    }

    fun createChat(id:Int, name: String, pk:String, context: Context){
        var pair = CryptographyService.generateSymmetricKey()
        key = pair.first
        var encodedKey = CryptographyService.encryptAsym(pk, key!!)
        runBlocking {
            val job = GlobalScope.async {
                ApolloClientService.createChat(id, name, encodedKey)
            }
            val result = job.await()

            if(result!= null) {
                CryptographyService.saveToSharedPreference("CHAT $result KEY", key!!, context)
                CryptographyService.saveToSharedPreference("CHAT $result IV", pair.second!!, context)
            }
            }

    }

    fun acceptChat(id:Int, myId:Int, context: Context){
        runBlocking {
            val job = GlobalScope.async {
                ApolloClientService.acceptChat(id)
            }
            val result = job.await()

            if(result!= null) {
                var priv = CryptographyService.getFromSharedPreference("PRIVATE $myId", context)
                var k = CryptographyService.decryptAsym(priv!!, result.second)
                CryptographyService.saveToSharedPreference("CHAT ${result.first} KEY", k, context)
               // CryptographyService.saveToSharedPreference("CHAT ${result.first} IV", pair.second!!, context)
            }
        }
    }
}