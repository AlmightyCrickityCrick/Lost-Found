package com.example.lostfound.utils.memento

import com.example.lostfound.activities.CreateAnnouncementActivity

data class AnnouncementSnapshot(var context : CreateAnnouncementActivity, var title:String, var details:String, var type :String, var tag :String ) :Snapshot {
    override fun restore() {
        context.restore(title, details, type, tag)
    }
}