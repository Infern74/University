package com.example.app.ui.home.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.data.Day
import com.example.app.utils.DateUtils

class DayAdapter(
    private val days: List<Day>,
    private val onDayClick: (org.threeten.bp.LocalDate) -> Unit
) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(day: Day) {
            itemView.findViewById<TextView>(R.id.tvDate).text = day.getFormattedDate()
            itemView.findViewById<TextView>(R.id.tvDayOfWeek).text = day.getDayOfWeek()

            // Подсветка текущего дня
            val isToday = DateUtils.isToday(day.date)
            itemView.setBackgroundColor(
                if (day.isSelected) Color.LTGRAY else Color.TRANSPARENT
            )
            itemView.findViewById<TextView>(R.id.tvDate).setTextColor(
                if (isToday) Color.RED else Color.BLACK
            )

            itemView.setOnClickListener { onDayClick(day.date) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = days.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(days[position])
    }

}