package com.example.app.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.data.Lesson

class LessonAdapter(private val lessons: List<Lesson>) :
    RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(lesson: Lesson) {
            itemView.findViewById<TextView>(R.id.tvTime).text = lesson.time
            itemView.findViewById<TextView>(R.id.tvTitle).text = lesson.title
            itemView.findViewById<TextView>(R.id.tvRoom).text = "${lesson.room}, ${lesson.building}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lessons.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lessons[position])
    }

}