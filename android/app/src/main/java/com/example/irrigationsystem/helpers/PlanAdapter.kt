package com.example.irrigationsystem.helpers

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.irrigationsystem.databinding.PlanBottomsheetRecyclerviewItemBinding
import com.example.irrigationsystem.models.Plan
import kotlinx.android.synthetic.main.plan_bottomsheet_recyclerview_item.view.*

class PlanAdapter(val clickListener : PlanListener) : ListAdapter<Plan, PlanAdapter.PlanHolder>(PlanDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanHolder {
        return PlanHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: PlanHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

    class PlanHolder private constructor(val binding : PlanBottomsheetRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data : Plan, listener:PlanListener){
            binding.plan = data
            binding.clickListener = listener
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

class PlanListener(val clickListener : (planId : Int) -> Unit){
    fun onClick(plan : Plan) = clickListener(plan.PlanId)
}