package com.example.irrigationsystem.helpers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.irrigationsystem.databinding.RecyclerviewItemBinding
import com.example.irrigationsystem.models.ScheduledDaysView

class DaysAdapter : ListAdapter<ScheduledDaysView, DaysAdapter.DayHolder>(DaysDiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
      return DayHolder.inflate(parent)
    }


    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DayHolder private constructor(val binding : RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data : ScheduledDaysView){
            binding.scheduledDays = data
            binding.executePendingBindings()
        }

        companion object{
            fun inflate(parent : ViewGroup) : DayHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerviewItemBinding.inflate(layoutInflater, parent, false)
                return DayHolder(binding)
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

