package com.example.lostfound.utils.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lostfound.R
import com.example.lostfound.data.Result
import com.example.lostfound.utils.ApolloClientService
import com.example.lostfound.utils.login.LoginResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class AnnouncementViewModel {
    protected val _annResult = MutableLiveData<AnnouncementResult>()
    val annResult: LiveData<AnnouncementResult> = _annResult
    fun create(type:String, content:String, location:String, tag:String, title:String, usrId: Int){
        runBlocking {
            var job = GlobalScope.async{ApolloClientService.createAnnouncement(type, content, location, tag, title, usrId)}
            var result = job.await()

            if(result is Result.Success){
                _annResult.value = AnnouncementResult(success = result.data.id?.toInt())
            } else{
                _annResult.value = AnnouncementResult(error = R.string.ann_create_failed)
            }
        }
    }
}