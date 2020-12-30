package com.example.irrigationsystem.helpers

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.irrigationsystem.R
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.ScheduledDaysView
import com.example.irrigationsystem.models.SetupInfo
import com.example.irrigationsystem.models.weatherapi.Forecast
import com.example.irrigationsystem.models.weatherapi.WeatherObject
import java.text.SimpleDateFormat

@BindingAdapter("DateSetter")
fun TextView.convertDateToString(value : Long){

    val format = SimpleDateFormat("dd-MMM HH:mm")
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

@BindingAdapter("BottomSheetDeleteButtonStatus")
fun ImageView.buttonStatus(value : Plan?){
    value?.let {
        visibility = if(value.IsActive){
            View.GONE
        } else{
            View.VISIBLE
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
            setBackgroundColor(Color.parseColor("#795548"))
            setTextColor(resources.getColor(R.color.colorOnSecondary,null))
            setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_baseline_cached_24,0)
            "DISCONNECTED"
        }
    }
}

@BindingAdapter("WeatherCity")
    fun TextView.cityText(obj : WeatherObject?){
        obj?.let {
            text = obj.city.cityName
        }
    }

@BindingAdapter("WeatherCountry")
fun TextView.countryText(obj : WeatherObject?){
    obj?.let {
        text = obj.city.cityCountry
    }
}

@BindingAdapter("ForecastDateTime")
fun TextView.forecastDateTime(obj : Forecast?){
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    obj?.let {
        val date = format.parse(obj.timeStampString)
        val dateLong = date.time
        text = "${SimpleDateFormat("dd-MM HH:mm").format(dateLong)}"
    }
}

@BindingAdapter("ForecastTemperature")
fun TextView.minMaxTemperature(obj:Forecast?){
    obj?.let {
        text = "${obj.weatherMainValues.currentTemp.toInt()}°C"
    }
}

@BindingAdapter("imgUrl")
fun bindImage(imgView : ImageView, forecast : Forecast?){

    forecast?.let{
        val imageUrl = "http://openweathermap.org/img/wn/${forecast.weatherType[0].typeIcon}@2x.png"
        val imageUri = imageUrl.toUri().buildUpon().scheme("http").build()

        Glide.with(imgView.context)
            .load(imageUri)
            .apply(RequestOptions().placeholder(R.drawable.ic_baseline_image_24).error(R.drawable.ic_baseline_broken_image_24))
            .into(imgView)
    }
}

@BindingAdapter("tempValue","setupInfo")
fun TextView.temperatureValueWithIndicator(tempValue : LiveData<String>, setupInfo : LiveData<SetupInfo>){
    val setupInfoValue = setupInfo.value
    tempValue.value.let {

        if (!it.isNullOrEmpty()) {
            if (setupInfoValue != null) {
                val doubleValue: Double = it.toDouble()
                when {
                    doubleValue > setupInfoValue.TemperatureMaxLimit -> {
                        text = "$it°C"
                        setTextColor(resources.getColor(R.color.Danger, null))
                    }
                    doubleValue < setupInfoValue.TemperatureMinLimit -> {
                        text = "$it°C"
                        setTextColor(resources.getColor(R.color.Cold, null))
                    }
                    else -> {
                        text = it
                        setTextColor(resources.getColor(R.color.black, null))
                    }
                }
            } else {
                text = "$it°C"
                setTextColor(resources.getColor(R.color.black, null))
            }
        }else text = "NaN"
    }
}

@BindingAdapter("hummValue","setupInfo2")
fun TextView.hummidityValueWithIndicator(hummValue : LiveData<String>, setupInfo2 : LiveData<SetupInfo>){
    val setupInfoValue = setupInfo2.value
    hummValue.value.let {

        if(!it.isNullOrEmpty()) {
            if (setupInfoValue != null) {
                val doubleValue: Double = it.toDouble()
                when {
                    doubleValue > setupInfoValue.HummidityMaxLimit -> {
                        text = "$it%"
                        setTextColor(resources.getColor(R.color.Danger, null))
                    }
                    doubleValue < setupInfoValue.HummidityMinLimit -> {
                        text = "$it%"
                        setTextColor(resources.getColor(R.color.Cold, null))
                    }
                    else -> {
                        text = "$it%"
                        setTextColor(resources.getColor(R.color.black, null))
                    }
                }
            } else {
                text = it
                setTextColor(resources.getColor(R.color.black, null))
            }
        } else text = "NaN"
    }
}

@BindingAdapter("soilMoistureValue")
fun TextView.soilMoistureValue(moistureValue : LiveData<String>){

    moistureValue.value.let {
        text = if(it.isNullOrEmpty()) "NaN"
        else it
    }
}





