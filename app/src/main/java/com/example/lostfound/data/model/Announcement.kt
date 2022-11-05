package com.example.lostfound.data.model

data class Announcement(var found_ann_id:Int, val ann_title :String, val user:User, val tags:ArrayList<String>, val content:String)

data class User(val user_id:Int, var user_name:String, var user_pic:Int)