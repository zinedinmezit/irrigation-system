package com.example.irrigationsystem.helpers

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageView
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
import java.util.*

@BindingAdapter("DateSetter")
fun TextView.convertDateToString(value : Long){

    val format = SimpleDateFormat("dd-MMM HH:mm", Locale.getDefault())
    val date = format.format(TypeConverters.fromTimestamp(value))
    text = date.toString()
}

@BindingAdapter("TimeSetter")
fun TextView.convertDateToTimeString(value : Long){
    val format = SimpleDateFormat("HH:mm",Locale.getDefault())
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

@BindingAdapter("backgroundBehaviour")
fun Button.backgroundBehaviour(state : Boolean){

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
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    obj?.let {
        val date = format.parse(obj.timeStampString)
        val dateLong = date?.time
        text = SimpleDateFormat("dd-MM HH:mm", Locale.getDefault()).format(dateLong)
    }
}

@BindingAdapter("forecastTemperature")
fun TextView.forecastTemperature(obj:Forecast?){
    obj?.let {
        text = resources.getString(R.string.forecastTemperature, obj.weatherMainValues.currentTemp.toInt())
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
    tempValue.value.let { value ->

        if (!value.isNullOrEmpty()) {
            if (setupInfoValue != null) {
                val doubleValue: Double = value.toDouble()
                when {
                    doubleValue > setupInfoValue.TemperatureMaxLimit -> {
                        text = resources.getString(R.string.sensorTemperature, value.toDouble())
                        setTextColor(resources.getColor(R.color.Danger, null))
                    }
                    doubleValue < setupInfoValue.TemperatureMinLimit -> {
                        text = resources.getString(R.string.sensorTemperature, value.toDouble())
                        setTextColor(resources.getColor(R.color.Cold, null))
                    }
                    else -> {
                        text = value
                        setTextColor(resources.getColor(R.color.black, null))
                    }
                }
            } else {
                text = resources.getString(R.string.sensorTemperature, value.toFloat())
                setTextColor(resources.getColor(R.color.black, null))
            }
        }else text = resources.getString(R.string.not_a_number)
    }
}

@BindingAdapter("hummValue","setupInfo2")
fun TextView.hummidityValueWithIndicator(hummValue : LiveData<String>, setupInfo2 : LiveData<SetupInfo>){
    val setupInfoValue = setupInfo2.value
    hummValue.value.let { value ->

        if(!value.isNullOrEmpty()) {
            if (setupInfoValue != null) {
                val doubleValue: Double = value.toDouble()
                when {
                    doubleValue > setupInfoValue.HummidityMaxLimit -> {
                        text = resources.getString(R.string.hummidityPercentage, value)
                        setTextColor(resources.getColor(R.color.Danger, null))
                    }
                    doubleValue < setupInfoValue.HummidityMinLimit -> {
                        text = resources.getString(R.string.hummidityPercentage, value)
                        setTextColor(resources.getColor(R.color.Cold, null))
                    }
                    else -> {
                        text = resources.getString(R.string.hummidityPercentage, value)
                        setTextColor(resources.getColor(R.color.black, null))
                    }
                }
            } else {
                text = value
                setTextColor(resources.getColor(R.color.black, null))
            }
        } else text = resources.getString(R.string.not_a_number)
    }
}

@BindingAdapter("soilMoistureValue")
fun TextView.soilMoistureValue(moistureValue : LiveData<String>){

    moistureValue.value.let {
        text = if(it.isNullOrEmpty()) resources.getString(R.string.not_a_number)
        else it
    }
}





