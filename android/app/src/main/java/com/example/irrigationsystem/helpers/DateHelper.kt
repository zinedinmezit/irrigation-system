package com.example.irrigationsystem.helpers

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private val chipIdMap = mapOf(
        2131230828 to 2, 2131230829 to 3, 2131230830 to 4,
        2131230831 to 5, 2131230832 to 6, 2131230833 to 7,
        2131230834 to 1
    )

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")


    fun getDateForCurrentSchedule(list : MutableList<Int>) : Pair<Date,MutableList<Int>>{

        var dayForSchedule = 0
        val listTransformed = transformListIds(list)
        val c = Calendar.getInstance()

        var current = c.get(Calendar.DAY_OF_WEEK)
        val filteredList = listTransformed.filter { it -> it > current }
        if(filteredList.count() > 0) {
            dayForSchedule = filteredList[0]

            while (current != dayForSchedule){
                c.add(Calendar.DATE,1)
                current = c.get(Calendar.DAY_OF_WEEK)
                Log.i("testtest2","$current")
                Log.i("testtest2","${c.time}")
            }
        }else{
            listTransformed.sort()
            dayForSchedule = listTransformed[0]
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

        val newDateStr = dateFormat.format(c.time)

        return  Pair(dateFormat.parse(newDateStr)!!, listTransformed)
    }

     fun transformListIds(list : MutableList<Int>) : MutableList<Int>{

        val listTransformed = mutableListOf<Int>()
        list.forEach { it -> chipIdMap[it]?.let { it1 -> listTransformed.add(it1) } }

        return listTransformed
    }
}