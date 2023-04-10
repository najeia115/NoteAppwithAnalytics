package com.example.noteapp.Adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.Model.Note
import com.example.noteapp.NoteDetails
import com.example.noteapp.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore

class NoteAdapter( var data : ArrayList<Note>) : RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    private lateinit var db: FirebaseFirestore

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titleTextView: TextView = itemView.findViewById(R.id.noteTitletv)
        var contentTextView: TextView = itemView.findViewById(R.id.noteContenttv)
        var itemCard : CardView = itemView.findViewById(R.id.NoteItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = data[position]
        holder.titleTextView.text = note.Title
        holder.contentTextView.text = note.Description

        holder.itemCard.setOnClickListener {

            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "noteScreen02")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "Note")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "cardItem")
            }
            FirebaseAnalytics.getInstance(holder.titleTextView.context).logEvent(
                FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

            val intent = Intent(holder.titleTextView.context, NoteDetails::class.java)
            intent.putExtra("noteId",note.Id)
            intent.putExtra("noteTitle",note.Title)
            Log.d("noteAdapterId", "noteId: ${note.Id}")
            holder.itemView.context.startActivity(intent)
        }
    }
}