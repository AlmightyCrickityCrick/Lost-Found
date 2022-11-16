package com.example.lostfound.data.model

data class ChatMessage(var senderId:Int, var receiverId:Int, val message:String, var dateTime:String)