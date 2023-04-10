package com.example.noteapp.Adapter

import android.content.Context
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
import com.example.noteapp.Model.Category
import com.example.noteapp.NoteActivity
import com.example.noteapp.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore

class CategoryAdapter( var categoryData : ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var categoryNameTextView: TextView = itemView.findViewById(R.id.CategoryNametv)
        var itemCard : CardView = itemView.findViewById(R.id.CategoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = categoryData.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = categoryData[position]
        holder.categoryNameTextView.text = category.categoryName


        holder.itemCard.setOnClickListener {

            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, "categoryScreen01")
                putString(FirebaseAnalytics.Param.ITEM_NAME, "category")
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "cardItem")
            }
            FirebaseAnalytics.getInstance(holder.categoryNameTextView.context).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

            val intent = Intent(holder.categoryNameTextView.context, NoteActivity::class.java)
            intent.putExtra("categoryId", category.id)
            Log.d("adapterId", "categoryId: ${category.id}")
            holder.itemView.context.startActivity(intent)
        }
    }

}