package com.example.irrigationsystem.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_secondary.*
import java.util.*

class TimePickerFragment(val fragmentCode : Int) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    lateinit var ctx : Context


    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(context, this, hour, minute, true)
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val activity : Activity = ctx as Activity
        var timeEditText : EditText? = null

        when(fragmentCode){
            1 -> timeEditText = activity.editTextTime
            2 -> timeEditText = activity.edit_timeText
        }

        var hourString : String = hourOfDay.toString()
        var minuteString : String = minute.toString()
        if(hourOfDay < 10){
             hourString = "0${hourOfDay}"
        }
        if(minute < 10){
            minuteString = "0${minute}"
        }
        timeEditText?.setText("${hourString}:${minuteString}")
    }
}