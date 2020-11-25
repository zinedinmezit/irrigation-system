package com.example.irrigationsystem.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

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

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    @SuppressLint("SimpleDateFormat")
    val dateFormatWithTime = SimpleDateFormat("dd-MM-yyyy HH:mm")


    fun getDateForCurrentSchedule(list : MutableList<Int>, timeString:String) : Pair<Date,MutableList<Int>>{

        val dayForSchedule: Int
        val c = Calendar.getInstance()

        var current = c.get(Calendar.DAY_OF_WEEK)
        val filteredList = list.filter { it -> it > current }
        if(filteredList.count() > 0) {
            dayForSchedule = filteredList[0]

            while (current != dayForSchedule){
                c.add(Calendar.DATE,1)
                current = c.get(Calendar.DAY_OF_WEEK)
            }
        }else{
            list.sort()
            dayForSchedule = list[0]
            if(dayForSchedule == current){
                c.add(Calendar.DATE,1)
                current = c.get(Calendar.DAY_OF_WEEK)
                while (current != dayForSchedule){
                    c.add(Calendar.DATE,1)
                    current = c.get(Calendar.DAY_OF_WEEK)
                }
            }
            else {
                while (current != dayForSchedule) {
                    c.add(Calendar.DATE, 1)
                    current = c.get(Calendar.DAY_OF_WEEK)
                }
            }
        }

        val newDateStr = "${dateFormat.format(c.time)} $timeString"
        return  Pair(dateFormatWithTime.parse(newDateStr)!!, list)
    }

     fun transformListIds(list : MutableList<Int>, listCode : Int = 1) : MutableList<Int>{

        val listTransformed = mutableListOf<Int>()
         if(listCode == 1) {
             list.forEach { it -> chipIdMap[it]?.let { it1 -> listTransformed.add(it1) } }
         }
         else if(listCode == 2){
             list.forEach { it -> chipIdMapE[it]?.let { it1 -> listTransformed.add(it1) } }
         }


        return listTransformed
    }
}