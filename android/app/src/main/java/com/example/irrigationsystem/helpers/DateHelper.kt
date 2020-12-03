package com.example.irrigationsystem.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    //Maps for 3 groups of chips so we can transform their ids with DAY_OF_WEEK

    private val chipIdMap = mapOf(
        2131230832 to 2, 2131230833 to 3, 2131230834 to 4,
        2131230835 to 5, 2131230836 to 6, 2131230837 to 7,
        2131230838 to 1
    )

    private val chipIdMapE = mapOf(
        2131230839 to 2, 2131230840 to 3, 2131230841 to 4,
        2131230842 to 5, 2131230843 to 6, 2131230844 to 7,
        2131230845 to 1
    )

    private val chipIdMapS = mapOf(
        2131362144 to 2, 2131362145 to 3, 2131362146 to 4,
        2131362147 to 5, 2131362148 to 6, 2131362149 to 7,
        2131362150 to 1
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