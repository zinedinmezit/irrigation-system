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