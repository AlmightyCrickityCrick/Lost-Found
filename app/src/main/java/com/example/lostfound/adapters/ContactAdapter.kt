package com.example.lostfound.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.R
import com.example.lostfound.data.model.Contact
import com.example.lostfound.listeners.ContactListener

class ContactAdapter(var contacts:ArrayList<Contact>, var contactListener: ContactListener) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_contact, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactCardViewHolder) populateItemRows(holder, position)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    private class ContactCardViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var user_img :ImageView
        var user_name:TextView
        var message:TextView
        var time:TextView
        var card : CardView
        init {
            user_img = itemView.findViewById(R.id.chat_contact_image)
            user_name = itemView.findViewById(R.id.chat_contact_username)
            message = itemView.findViewById(R.id.chat_contact_last_message)
            time = itemView.findViewById(R.id.chat_contact_time_last_messaged)
            card = itemView.findViewById(R.id.chat_contact_card)
        }
    }
    private fun populateItemRows(viewHolder: ContactCardViewHolder, position: Int){
        var item = contacts.get(position)
        viewHolder.user_img.setImageResource(item.user.user_pic)
        viewHolder.user_name.text = item.user.user_name
        viewHolder.message.text = item.last_message
        viewHolder.time.text = item.time_last_message
        viewHolder.card.setOnClickListener{v-> contactListener.onContactClicked(item)}

    }
}