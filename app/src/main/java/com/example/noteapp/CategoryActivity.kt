package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.Adapter.CategoryAdapter
import com.example.noteapp.Model.Category
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class CategoryActivity : AppCompatActivity() {


    private lateinit var rvCategory: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private var categoryArrayList = ArrayList<Category>()
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private var calendar: Calendar = Calendar.getInstance()
    private var hour: Int = calendar.get(Calendar.HOUR)
    private var minute: Int = calendar.get(Calendar.MINUTE)
    private var second: Int = calendar.get(Calendar.SECOND)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        getCategoryDataFromFirestore()

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        screenTrack("Category Screen")

        rvCategory = findViewById(R.id.CategoryRV)
        categoryAdapter = CategoryAdapter(ArrayList())
        rvCategory.layoutManager = LinearLayoutManager(this)
        rvCategory.adapter = categoryAdapter
        categoryAdapter.notifyDataSetChanged()


    }

    private fun getCategoryDataFromFirestore() {
        var db: FirebaseFirestore = Firebase.firestore
        db.collection("categories")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val category = document.toObject(Category::class.java)
                    category.categoryName = document.getString("name").toString()
                    categoryArrayList.add(category)
                }
                categoryAdapter = CategoryAdapter(categoryArrayList)
                val rvCategory: RecyclerView = findViewById(R.id.CategoryRV)
                rvCategory.adapter = categoryAdapter
            }

            .addOnFailureListener {
                Log.e("najeia", "Failed to load Categories ")
            }

    }

    fun screenTrack(name: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Category Activity")
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
        users["screenName"] = "Category screen"

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