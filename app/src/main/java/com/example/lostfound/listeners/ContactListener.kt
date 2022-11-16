package com.example.lostfound.listeners

import com.example.lostfound.data.model.Contact

interface ContactListener {
    fun onContactClicked(contact:Contact)
}