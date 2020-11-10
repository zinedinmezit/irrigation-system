package com.example.irrigationsystem.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.irrigationsystem.R
import com.example.irrigationsystem.models.ScheduledDaysView

class DaysAdapter() : ListAdapter<ScheduledDaysView, DaysAdapter.DayHolder>(DaysDiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
      return DayHolder.inflate(parent)
    }


    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DayHolder private constructor(v : View) : RecyclerView.ViewHolder(v){
        private val dayText : TextView = v.findViewById(R.id.day_id)

        fun bind(data : ScheduledDaysView){
            dayText.text = data.Name
        }

        companion object{
            fun inflate(parent : ViewGroup) : DayHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,parent,false)
                return DayHolder(view)
            }
        }
    }
}

class DaysDiffCallback : DiffUtil.ItemCallback<ScheduledDaysView>(){
    override fun areItemsTheSame(oldItem: ScheduledDaysView, newItem: ScheduledDaysView): Boolean {
        return oldItem.OrdinalNumber == newItem.OrdinalNumber
    }

    override fun areContentsTheSame(
        oldItem: ScheduledDaysView,
        newItem: ScheduledDaysView
    ): Boolean {
        return oldItem==newItem
    }

}

