package com.example.irrigationsystem.helpers

import android.widget.TextView
import androidx.databinding.BindingAdapter
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