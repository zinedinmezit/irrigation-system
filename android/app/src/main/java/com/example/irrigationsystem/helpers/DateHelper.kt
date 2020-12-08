package com.example.irrigationsystem.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    //Maps for 3 groups of chips so we can transform their ids with DAY_OF_WEEK

    private val chipIdMap = mapOf(
        2131361906 to 2, 2131361907 to 3, 2131361908 to 4,
        2131361909 to 5, 2131361910 to 6, 2131361911 to 7,
        2131361912 to 1
    )

    private val chipIdMapE = mapOf(
        2131361913 to 2, 2131361914 to 3, 2131361915 to 4,
        2131361916 to 5, 2131361917 to 6, 2131361918 to 7,
        2131361919 to 1
    )

    private val chipIdMapS = mapOf(
        2131362158 to 2, 2131362159 to 3, 2131362160 to 4,
        2131362161 to 5, 2131362162 to 6, 2131362163 to 7,
        2131362164 to 1
    )

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    @SuppressLint("SimpleDateFormat")
    val dateFormatWithTime = SimpleDateFormat("dd-MM-yyyy HH:mm")

    fun getDateForCurrentSchedule(selectedDays : MutableList<Int>, clockTimeString:String) : Pair<Date,MutableList<Int>>{

        val dayToSchedule: Int
        val myCalendar = Calendar.getInstance()

        var currentDay = myCalendar.get(Calendar.DAY_OF_WEEK)
        val filteredList = selectedDays.filter { it -> it > currentDay }
        if(filteredList.count() > 0) {
            dayToSchedule = filteredList[0]
            while (currentDay != dayToSchedule){
                myCalendar.add(Calendar.DATE,1)
                currentDay = myCalendar.get(Calendar.DAY_OF_WEEK)
            }
        }
        else{
            selectedDays.sort()
            dayToSchedule = selectedDays[0]
            if(dayToSchedule == currentDay){
                myCalendar.add(Calendar.DATE,1)
                currentDay = myCalendar.get(Calendar.DAY_OF_WEEK)
                while (currentDay != dayToSchedule){
                    myCalendar.add(Calendar.DATE,1)
                    currentDay = myCalendar.get(Calendar.DAY_OF_WEEK)
                }
            }
            else{
                while (currentDay != dayToSchedule) {
                    myCalendar.add(Calendar.DATE, 1)
                    currentDay = myCalendar.get(Calendar.DAY_OF_WEEK)
                }
            }
        }

        val newDateStr = "${dateFormat.format(myCalendar.time)} $clockTimeString"
        return  Pair(dateFormatWithTime.parse(newDateStr)!!, selectedDays)
    }

     fun transformListIds(list : MutableList<Int>, listCode : Int = 1) : MutableList<Int>{

        val listTransformed = mutableListOf<Int>()
         when (listCode) {
             1 -> {
                 list.forEach { it -> chipIdMap[it]?.let { it1 -> listTransformed.add(it1) } }
             }
             2 -> {
                 list.forEach { it -> chipIdMapE[it]?.let { it1 -> listTransformed.add(it1) } }
             }
             3 -> {
                 list.forEach { it -> chipIdMapS[it]?.let { it1 -> listTransformed.add(it1) } }
             }
         }

        return listTransformed
    }
}