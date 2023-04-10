package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.Adapter.CategoryAdapter
import com.example.noteapp.Adapter.NoteAdapter
import com.example.noteapp.Model.Category
import com.example.noteapp.Model.Note
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class NoteActivity : AppCompatActivity() {

    private lateinit var rvNotes: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    val db = FirebaseFirestore.getInstance()
    private var noteArrayList = ArrayList<Note>()
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private var calendar: Calendar = Calendar.getInstance()
    private var hour: Int = calendar.get(Calendar.HOUR)
    private var minute: Int = calendar.get(Calendar.MINUTE)
    private var second: Int = calendar.get(Calendar.SECOND)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        rvNotes = findViewById(R.id.notesRV)
        noteAdapter = NoteAdapter(ArrayList())
        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = noteAdapter
        noteAdapter.notifyDataSetChanged()
        getNoteFromFirestore()

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        screenTrack("Note Screen")
    }

    fun getNoteFromFirestore() {
        db.collection("notes")
            .whereEqualTo("categoryId", intent.getStringExtra("categoryId")).get()
            .addOnSuccessListener {
                for (document in it) {
                    val note = document.toObject(Note::class.java)
                    note.Id = document.id
                    noteArrayList.add(note)
                }

                Handler(Looper.getMainLooper()).post {
                    noteAdapter = NoteAdapter(noteArrayList)
                    val rvNotes: RecyclerView = findViewById(R.id.notesRV)
                    rvNotes.adapter = noteAdapter
                }

            }

            .addOnFailureListener {
                Log.e("najeia", "Failed to load Categories ")
            }
    }

    fun screenTrack(name: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Note Activity")
        Log.e("analytics", bundle.toString())
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }


    override fun onPause() {
        val c: Calendar = Calendar.getInstance()
        val hour2: Int = c.get(Calendar.HOUR_OF_DAY)
        val minute2: Int = c.get(Calendar.MINUTE)
        val second2: Int = c.get(Calendar.SECOND)

        val h: Int = hour2 - hour
        val m: Int = minute2 - minute
        val s: Int = second2 - second

        Log.e("hour", h.toString())
        Log.e("minute", m.toString())
        Log.e("second", s.toString())

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val users: MutableMap<String, Any> = HashMap()
        users["hours"] = h
        users["minute"] = m
        users["second"] = s
        users["screenName"] = "Note screen"

        db.collection("users")
            .add(users)
            .addOnSuccessListener { DocumentReference ->
                // do something on success
            }
            .addOnFailureListener {
                // do something on failure
            }

        super.onPause()
    }

}