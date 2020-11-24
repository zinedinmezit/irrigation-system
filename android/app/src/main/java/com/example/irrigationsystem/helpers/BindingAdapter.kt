package com.example.irrigationsystem.helpers

import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
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

        if(value.IsActive){
            text = "${value.Name} (Current active)"
        }
        else{
            text = value.Name
        }
    }
}