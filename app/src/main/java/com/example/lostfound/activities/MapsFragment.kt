package com.example.lostfound.activities

import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.lostfound.R
import com.example.lostfound.data.model.MapAnn
import com.example.lostfound.databinding.AnnouncementCardMapBinding
import com.example.lostfound.databinding.FragmentMapsBinding
import com.example.lostfound.utils.ApolloClientService
import com.example.lostfound.utils.setMenuButton

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.nio.file.Files.find

class MapsFragment : Fragment() {
    private var isSelected:Int? = null
    private var annArray:ArrayList<MapAnn>?=null
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        if(annArray!=null){
            for (ann in annArray!!){
                var m:Marker = googleMap.addMarker(MarkerOptions().position(ann.coords).title(ann.ann_title))
                m.tag = ann
                googleMap.setOnMarkerClickListener{marker ->
                    var x = marker.tag as MapAnn
                    addCard(x.ann_title, x.content, x.id)
                    true
                }
            }
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(47.003670, 28.907089)))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        var toolbar = view.findViewById<Toolbar>(R.id.map_toolbar)
        activity?.let { setMenuButton(toolbar, it) }
        getMapAnnouncements()
        mapFragment?.getMapAsync(callback)

    }

    private fun addCard(title:String, text:String, id:Int){
        var ann:LinearLayout? = view?.findViewById(R.id.map_ann_card)
        ann?.removeAllViews()
        val card = LayoutInflater.from(ann?.context).inflate(R.layout.announcement_card_map, null)
        var titleView :TextView= card.findViewById(R.id.announcement_title)
        titleView.text = title
        var textView :TextView = card.findViewById(R.id.announcement_text)
        textView.setText(text)
        val detailButton = card.findViewById<Button>(R.id.btn_navigate_ann_detail)
        ann?.addView(card)
        detailButton.setOnClickListener {
            activity?.let {
                var intent = Intent(it, AnnouncementDetailActivity::class.java)
                intent.putExtra("ID", id)
                startActivity(intent)
            }
        }
    }

    private fun getMapAnnouncements(){
            runBlocking {
                var job = async { ApolloClientService.getAllMapAnn() }
                var result = job.await()
                if(result.size>0) {
                    annArray = result
                }

            }
    }
}