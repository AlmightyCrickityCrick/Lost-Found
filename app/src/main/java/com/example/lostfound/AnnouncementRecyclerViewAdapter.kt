package com.example.lostfound

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfound.data.model.Announcement
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView

import androidx.annotation.NonNull

import android.widget.ProgressBar

import android.widget.TextView

class AnnouncementRecyclerViewAdapter(var ann:ArrayList<Announcement>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LostCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.lost_found_card, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is LostCardViewHolder){
            populateItemRows(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return ann.size
    }

    private class LostCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var user_img :ImageView
        var user_name:TextView
        var ann_title :TextView
        var ann_text:TextView

        init {
            user_img = itemView.findViewById(R.id.announcement_poster_img)
            user_name = itemView.findViewById(R.id.announcement_poster_name)
            ann_title=itemView.findViewById(R.id.announcement_title)
            ann_text=itemView.findViewById(R.id.announcement_text)
        }
    }


    private fun populateItemRows(viewHolder: LostCardViewHolder, position: Int) {
        var item = ann.get(position)
        viewHolder.ann_title.text= item.ann_title
        viewHolder.user_img.setImageResource(item.user.user_pic)
        viewHolder.ann_text.text=item.content
        viewHolder.user_name.text = item.user.user_name
    }

}