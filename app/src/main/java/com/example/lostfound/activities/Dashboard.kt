package com.example.lostfound.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.R
import com.example.lostfound.activities.dashboard_states.DashboardState
import com.example.lostfound.activities.dashboard_states.FoundState
import com.example.lostfound.activities.dashboard_states.LostState
import com.example.lostfound.adapters.AnnouncementRecyclerViewAdapter
import com.example.lostfound.data.DebugConstants
import java.util.*
import kotlin.concurrent.timerTask

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Dashboard : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var adapter: AnnouncementRecyclerViewAdapter
    var annArray= DebugConstants.getAnnouncements()
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var state :DashboardState
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    fun updateData(){
        var lst = annArray
        state.getAnnouncements()
        if(lst!=annArray){
            updateDashboard()
        }
    }
    fun updateDashboard() {
        Toast.makeText(this.context, annArray.size.toString(), Toast.LENGTH_LONG ).show()
        //adapter.notifyDataSetChanged()
        initAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.announcement_view)
        toolbar = view.findViewById(R.id.ann_dash_toolbar)
        initAdapter()
        state = LostState(this)

        var change_btn :ImageButton= view.findViewById(R.id.toolbar_btn_change_ann)
        change_btn.setOnClickListener {
            modifyState()

        }
        var add_btn :Button = view.findViewById(R.id.btn_dashboard_add_announcement)
        add_btn.setOnClickListener {
            activity?.let {
                var intent = Intent(it, CreateAnnouncementActivity::class.java)
                startActivity(intent)
            }

        }
        var t = Timer()
        t.scheduleAtFixedRate(
            timerTask {
                updateData()
            }
            , 600000, 600000)

    }
    private fun modifyState(){
        if(this.state.type == 0) {
            this.state = FoundState(this)
        }
        else {
            this.state = LostState(this)
        }
    }
    private fun initAdapter() {
        adapter = AnnouncementRecyclerViewAdapter(annArray)
        recyclerView.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Lost.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Dashboard().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}