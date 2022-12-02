package com.example.lostfound.activities.dashboard_states

import android.widget.TextView
import com.example.lostfound.R
import com.example.lostfound.activities.Dashboard

class FoundState(dashboard: Dashboard) : DashboardState(dashboard) {
    override var type = 1
    init {
        initiateStateChange()
    }
    override fun initiateStateChange() {
        setToolbar()
        getAnnouncements()
    }

    override fun setToolbar() {
        var name : TextView = dashboard.toolbar.findViewById(R.id.toolbar_fragment_name)
        name.setText(R.string.found)
    }

    override fun getAnnouncements() {
        print("hi")
    }
}