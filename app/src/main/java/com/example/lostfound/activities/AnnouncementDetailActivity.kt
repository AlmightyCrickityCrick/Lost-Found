package com.example.lostfound.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.lostfound.R
import com.example.lostfound.data.model.AnnDetails
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.data.model.User
import com.example.lostfound.databinding.ActivityAnnouncementDetailBinding
import com.example.lostfound.databinding.ActivityCreateAnnouncementBinding
import com.example.lostfound.utils.announcement.AnnouncementDetailController
import com.squareup.picasso.Picasso

class AnnouncementDetailActivity : AppCompatActivity() {
    var id :Int?=null
    lateinit var binding: ActivityAnnouncementDetailBinding
    lateinit var title: TextView
    lateinit var content:TextView
    lateinit var tags:TextView
    lateinit var location:TextView
    lateinit var user: TextView
    lateinit var imageView: ImageView
    lateinit var annDetail:AnnDetails


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityAnnouncementDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getSerializableExtra("ID") as Int
        Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()

        var detailController = AnnouncementDetailController()
        if(id!=null)detailController.getAnnouncement(id!!)

        detailController.annDet.observe(this@AnnouncementDetailActivity, Observer {
            var details = it ?: return@Observer
            annDetail = details
            changeData()
        })


        title = binding.annTextTitle
        content = binding.annTextContent
        tags = binding.annTagList
        location = binding.annLocationStreet
        user = binding.annUserUsername
        imageView = binding.annImg



    }

    fun changeData(){
        title.text = annDetail.ann_title
        content.text = annDetail.content
        var t = ""
        for (s in annDetail.tags) t = "$t $s"
        tags.text = t
        location.text = annDetail.streetName
        t = "${annDetail.user.user_name} (${annDetail.user.Rating})"
        user.text = t
        if(annDetail.image != "") Picasso.get().load("http://10.0.2.2:8000/media/${annDetail.image}").resize(1020, 0).into(imageView)


    }
}