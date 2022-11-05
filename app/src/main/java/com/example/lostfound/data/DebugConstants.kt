package com.example.lostfound.data

import com.example.lostfound.R
import com.example.lostfound.data.model.Announcement
import com.example.lostfound.data.model.Contact
import com.example.lostfound.data.model.User

object DebugConstants {
    fun getAnnouncements(): ArrayList<Announcement>{
        var x = ArrayList<Announcement>()
        for (i in 1 .. 10)
            x.add(Announcement(1, "I lost my Wallet!!", User(1, "User Name", R.drawable.ic_chat), arrayListOf("Horror", "tragedy"), "Lorem ipsum! Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."))
    return x
    }
    fun getContacts():ArrayList<Contact>{
        var x = ArrayList<Contact>()
        for (i in 1.. 6) x.add(Contact(User(1, "User Name2", R.drawable.ic_account), "Hello, I fund something that belongs to you", "23:5${i} pm"))
    return x
    }
}