package com.example.lostfound.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.R
import com.example.lostfound.data.model.Contact
import com.example.lostfound.listeners.ContactListener

class ContactAdapter(var contacts:ArrayList<Contact>, var contactListener: ContactListener) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val VIEW_TYPE_ACTIVE = 1
    val VIEW_TYPE_INACTIVE= 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //TODO:Might have to change viewType to getItemViewType
        if(viewType == VIEW_TYPE_ACTIVE) return ContactCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_contact, parent, false))
        else return InactiveContactCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_contact_inactive,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position)==VIEW_TYPE_ACTIVE) populateActiveRows(holder as ContactCardViewHolder, position)
        else if (getItemViewType(position) == VIEW_TYPE_INACTIVE ) populateInactiveRows(holder as InactiveContactCardViewHolder, position)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun getItemViewType(position: Int): Int {
        //TODO:State Number properly
        if (contacts.get(position).state == 1) return VIEW_TYPE_ACTIVE else return VIEW_TYPE_INACTIVE
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
    private fun populateActiveRows(viewHolder: ContactCardViewHolder, position: Int){
        var item = contacts.get(position)
        viewHolder.user_img.setImageResource(item.user.user_pic)
        viewHolder.user_name.text = item.user.user_name
        viewHolder.message.text = item.last_message
        viewHolder.time.text = item.time_last_message
        viewHolder.card.setOnClickListener{v-> contactListener.onContactClicked(item)}

    }

    private class InactiveContactCardViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var user_img :ImageView
        var user_name:TextView
        var accept: Button
        var decline:Button
        init {
            user_img = itemView.findViewById(R.id.chat_contact_image)
            user_name = itemView.findViewById(R.id.chat_contact_username)
            accept = itemView.findViewById(R.id.chatlist_btn_accept)
            decline = itemView.findViewById(R.id.chatlist_btn_deny)
        }
    }
    private fun populateInactiveRows(viewHolder: InactiveContactCardViewHolder, position: Int){
        var item = contacts.get(position)
        viewHolder.user_img.setImageResource(item.user.user_pic)
        viewHolder.user_name.text = item.user.user_name
        viewHolder.accept.setOnClickListener{v-> contactListener.onAcceptClicked(item)}
        viewHolder.decline.setOnClickListener {v -> contactListener.onDeclineClicked(item)  }

    }
}