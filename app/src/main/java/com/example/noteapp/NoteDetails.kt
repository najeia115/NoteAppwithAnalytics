package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.noteapp.Model.Note
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class NoteDetails : AppCompatActivity() {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        val title: TextView = findViewById(R.id.textViewTitle)
        val description: TextView = findViewById(R.id.textViewDescription)
        val noteImage : ImageView = findViewById(R.id.noteimageView)
        val Id = intent.getStringExtra("noteId")
     //   val titleintent = intent.getStringExtra("noteTitle")

        val db = FirebaseFirestore.getInstance()
        db.collection("notes").document(Id.toString())
            .get()

            .addOnSuccessListener {document ->
                if (document != null) {
                    val note = document.toObject(Note::class.java)
//                    note.Id = Id.toString()
                    title.text = note!!.Title
                    description . text = note.Description
                    Glide.with(this).load(note.Image).into(noteImage)
                }
            }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Note Details Screen")
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Note Details")
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}