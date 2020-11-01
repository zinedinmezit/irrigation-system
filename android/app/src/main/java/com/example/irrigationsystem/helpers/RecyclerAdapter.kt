package com.example.irrigationsystem.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.irrigationsystem.R
import com.example.irrigationsystem.models.ScheduledDaysView

class DaysAdapter() : RecyclerView.Adapter<DaysAdapter.DayHolder>() {

    var data = listOf<ScheduledDaysView>()
        set(value) {
            field=value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
        return DayHolder(view)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        val item = data[position]
        holder.dayText.text = item.Name
    }

    override fun getItemCount(): Int = data.size


    class DayHolder(v : View) : RecyclerView.ViewHolder(v){
        val dayText : TextView = v.findViewById(R.id.day_id)
    }
}

