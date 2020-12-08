package com.example.irrigationsystem.helpers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.irrigationsystem.databinding.WeatherRecyclerviewItemBinding
import com.example.irrigationsystem.models.weatherapi.Forecast

class ForecastAdapter() : ListAdapter<Forecast, ForecastAdapter.ForecastHolder>(ForecastDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        return ForecastHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ForecastHolder private constructor(val binding : WeatherRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data : Forecast){
            binding.forecast = data
            binding.executePendingBindings()
        }

        companion object{
            fun inflate(parent : ViewGroup) : ForecastHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WeatherRecyclerviewItemBinding.inflate(layoutInflater, parent, false)
                return ForecastHolder(binding)
            }
        }
    }
}

class ForecastDiffCallback : DiffUtil.ItemCallback<Forecast>(){
    override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
        return oldItem.timeStampString == newItem.timeStampString
    }

    override fun areContentsTheSame(
        oldItem: Forecast,
        newItem: Forecast
    ): Boolean {
        return oldItem==newItem
    }

}