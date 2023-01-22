package com.example.lostfound.listeners

import com.example.lostfound.data.model.Announcement
import com.example.lostfound.data.model.Contact

interface AnnouncementListener {
    fun onAnnouncementClicked(ann: Announcement)

}