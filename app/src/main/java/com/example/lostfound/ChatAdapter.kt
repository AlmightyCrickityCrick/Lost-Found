package com.example.lostfound

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.data.model.ChatMessage
import com.example.lostfound.data.model.Contact
import com.example.lostfound.databinding.ItemContainerSentMessageBinding
import com.example.lostfound.listeners.ContactListener
import com.google.android.material.imageview.ShapeableImageView

class ChatAdapter(var messages:ArrayList<ChatMessage>, var receiverProfileImage: Int, var senderId:Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val VIEW_TYPE_SENT = 1
    val VIEW_TYPE_RECEIVED= 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SENT) return SentMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_container_sent_message, parent, false))
        else return ReceivedMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_container_received_message, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)== VIEW_TYPE_SENT){
            populateSent(holder as SentMessageViewHolder, position)
        } else {
            populateReceived(holder as ReceivedMessageViewHolder, position)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        if (messages.get(position).senderId == senderId) return VIEW_TYPE_SENT else return VIEW_TYPE_RECEIVED
    }

    private class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageText: TextView
        var timeMessage: TextView

        init {
            messageText = itemView.findViewById(R.id.message_text)
            timeMessage = itemView.findViewById(R.id.time_message)
        }

    }

    private class ReceivedMessageViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var messageText: TextView
        var timeMessage:TextView
        var img : ImageView
        init {
            messageText = itemView.findViewById(R.id.message_text)
            timeMessage = itemView.findViewById(R.id.time_message)
            img = itemView.findViewById(R.id.message_profile_image)
        }

    }

    private fun populateSent(viewHolder: SentMessageViewHolder, position: Int){
        var item = messages.get(position)
        viewHolder.messageText.setText(item.message)
        viewHolder.timeMessage.text = item.dateTime
    }
    private fun populateReceived(viewHolder: ReceivedMessageViewHolder, position: Int){
        var item = messages.get(position)
        viewHolder.messageText.setText(item.message)
        viewHolder.timeMessage.text = item.dateTime
        viewHolder.img.setImageResource(receiverProfileImage)
    }

    }
