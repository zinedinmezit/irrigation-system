package com.example.irrigationsystem.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateDaysHelper {

    private const val CALENDAR_SATURDAY = 7

    //Maps for 3 groups of chips so we can transform their ids with DAY_OF_WEEK

    private val chipIdCreateMap = mapOf(
        2131361908 to 2, 2131361909 to 3, 2131361910 to 4,
        2131361911 to 5, 2131361912 to 6, 2131361913 to 7,
        2131361914 to 1
    )

    private val chipIdEditMap = mapOf(
        2131361915 to 2, 2131361916 to 3, 2131361917 to 4,
        2131361918 to 5, 2131361919 to 6, 2131361920 to 7,
        2131361921 to 1
    )

    private val chipIdSetupMap = mapOf(
        2131362220 to 2, 2131362221 to 3, 2131362222 to 4,
        2131362223 to 5, 2131362224 to 6, 2131362225 to 7,
        2131362226 to 1
    )

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    @SuppressLint("SimpleDateFormat")
    val dateFormatWithTime = SimpleDateFormat("dd-MM-yyyy HH:mm")

    fun getDateForCurrentSchedule(selectedDays : MutableList<Int>, clockTimeString:String) : Pair<Date,MutableList<Int>>{

        val dayToSchedule : Int
        val myCalendar = Calendar.getInstance()

        val currentDay = myCalendar.get(Calendar.DAY_OF_WEEK)
        val filteredList = selectedDays.filter { it > currentDay }

        if(filteredList.count() > 0){
            dayToSchedule = filteredList[0]
            myCalendar.add(Calendar.DATE, dayToSchedule-currentDay)
        } else{
            selectedDays.sort()
            dayToSchedule = selectedDays[0]
            if(dayToSchedule == currentDay){
                myCalendar.add(Calendar.DATE,7)

            }else {
                myCalendar.add(Calendar.DATE, (CALENDAR_SATURDAY - currentDay)+dayToSchedule)
            }
        }

        val newDateStr = "${dateFormat.format(myCalendar.time)} $clockTimeString"
        return  Pair(dateFormatWithTime.parse(newDateStr)!!, selectedDays)
    }

     fun transformListIds(list : MutableList<Int>, listCode : Int = 1) : MutableList<Int>{

        val listTransformed = mutableListOf<Int>()
         when (listCode) {
             1 -> {
                 list.forEach { it -> chipIdCreateMap[it]?.let { it1 -> listTransformed.add(it1) } }
             }
             2 -> {
                 list.forEach { it -> chipIdEditMap[it]?.let { it1 -> listTransformed.add(it1) } }
             }
             3 -> {
                 list.forEach { it -> chipIdSetupMap[it]?.let { it1 -> listTransformed.add(it1) } }
             }
         }

        return listTransformed
    }
}
