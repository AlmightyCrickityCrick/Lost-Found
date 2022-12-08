package com.example.lostfound.utils.memento

import com.example.lostfound.activities.CreateAnnouncementActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class History(var context: CreateAnnouncementActivity){
    private lateinit var backup : AnnouncementSnapshot

    init {
       GlobalScope.launch (Dispatchers.Default){
           while (!context.isFinishing){
            delay(10000)
               makeBackup()
           }
        }

    }

    fun  makeBackup(){
        backup = context.createSnapshot()
    }

    fun undo(){
        backup.restore()
    }

}