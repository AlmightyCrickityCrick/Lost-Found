package com.example.lostfound.activities.dashboard_states

import android.widget.TextView
import com.example.lostfound.R
import com.example.lostfound.activities.Dashboard
import com.example.lostfound.data.model.Announcement
import com.example.lostfound.utils.ApolloClientService
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class LostState(dashboard: Dashboard) : DashboardState(dashboard) {

    override var type = 0
    init {
        initiateStateChange()
    }
    override fun initiateStateChange() {
        setToolbar()
        getAnnouncements()
    }

    override fun setToolbar() {
        var name : TextView = dashboard.toolbar.findViewById(R.id.toolbar_fragment_name)
        name.setText(R.string.lost)
    }

    override fun getAnnouncements(){
        runBlocking {
            var job = async { ApolloClientService.getAllLostAnn() }
            var result = job.await()
            if(result.size>0) {
                dashboard.annArray = result
                dashboard.updateDashboard()
            }
        }
    }
}