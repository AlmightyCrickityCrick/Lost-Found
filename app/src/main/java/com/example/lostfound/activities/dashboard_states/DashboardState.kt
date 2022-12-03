package com.example.lostfound.activities.dashboard_states

import com.example.lostfound.activities.Dashboard
import com.example.lostfound.data.model.Announcement

abstract class DashboardState(val dashboard:Dashboard) {
    abstract var type: Int
    abstract fun initiateStateChange()
    abstract fun setToolbar()
    abstract fun getAnnouncements()
}