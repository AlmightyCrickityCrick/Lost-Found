package com.example.lostfound.data.model

import com.google.android.gms.maps.model.LatLng

data class Announcement(var found_ann_id:Int, val ann_title :String, val user:User, val tags:ArrayList<String>, val content:String)

data class User(val user_id:Int, var user_name:String, var user_pic:Int, var Rating:Int?=null, var publicKey:String?=null)

data class MapAnn(var id:Int, var ann_title: String, var content: String, var coords:LatLng)

data class AnnDetails(var id: Int, var ann_title: String, var content: String, var user: User, var tags: ArrayList<String>, var streetName:String?=null, var image:String?=null)