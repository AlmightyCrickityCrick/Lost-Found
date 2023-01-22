package com.example.lostfound.utils.announcement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lostfound.data.model.AnnDetails
import com.example.lostfound.utils.ApolloClientService
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class AnnouncementDetailController {
    protected val _annDet = MutableLiveData<AnnDetails>()
    val annDet: LiveData<AnnDetails> = _annDet
    fun getAnnouncement(id:Int){
        runBlocking {
            var job = async { ApolloClientService.getAnnDetails(id) }
            var result = job.await()
            if(result!=null) {
                _annDet.value = result
            }
        }
    }

}