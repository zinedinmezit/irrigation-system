package com.example.irrigationsystem.helpers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.irrigationsystem.databinding.PlanBottomsheetRecyclerviewItemBinding
import com.example.irrigationsystem.models.Plan

class PlanAdapter : ListAdapter<Plan, PlanAdapter.PlanHolder>(PlanDiffCallback()) {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanHolder {
        return PlanHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: PlanHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class PlanHolder private constructor(val binding : PlanBottomsheetRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data : Plan){
            binding.plan = data
        }

        companion object{
            fun inflate(parent : ViewGroup) : PlanHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PlanBottomsheetRecyclerviewItemBinding.inflate(layoutInflater, parent, false)
                return PlanHolder(binding)
            }
        }
    }

}

class PlanDiffCallback : DiffUtil.ItemCallback<Plan>(){
    override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
       return oldItem.PlanId==newItem.PlanId
    }

    override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
        return oldItem==newItem
    }

}