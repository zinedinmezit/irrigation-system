package com.example.irrigationsystem.helpers

import android.graphics.Color
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.irrigationsystem.R
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.ScheduledDaysView
import java.text.SimpleDateFormat

@BindingAdapter("DateSetter")
fun TextView.convertDateToString(value : Long){

    val format = SimpleDateFormat("dd-MM-yyyy HH:mm")
    val date = format.format(TypeConverters.fromTimestamp(value))
    text = date.toString()
}

@BindingAdapter("TimeSetter")
fun TextView.convertDateToTimeString(value : Long){
    val format = SimpleDateFormat("HH:mm")
    val date = format.format(TypeConverters.fromTimestamp(value))
    text = date.toString()
}

@BindingAdapter("ScheduledDayViewString")
fun TextView.setScheduledDayString(value : ScheduledDaysView){

    value.let {
        text = value.Name
    }
}

@BindingAdapter("BottomSheetPlanName")
fun TextView.setPlanName(value : Plan){

    value.let {

        text = if(value.IsActive){
            "${value.Name} (Current active)"
        } else{
            value.Name
        }
    }
}

@BindingAdapter("BackgroundBehaviour")
fun Button.BackgroundBehaviour(state : Boolean){

    state.let {

        text = if(state) {
            setBackgroundColor(Color.parseColor("#00E315"))
            setTextColor(resources.getColor(R.color.black,null))
            setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
            "CONNECTED"

        } else{
            setBackgroundColor(Color.parseColor("#F45F55"))
            setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_baseline_cached_24,0)
            "DISCONNECTED"
        }
    }
}