package com.example.irrigationsystem.helpers

import android.graphics.Color
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.irrigationsystem.R
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.ScheduledDaysView
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

@BindingAdapter("ForecastMinMaxTemperature")
fun TextView.minMaxTemperature(obj:Forecast?){
    obj?.let {
        text = "${obj.weatherMainValues.minTemperature.toInt()}/${obj.weatherMainValues.maxTemperature.toInt()}"
    }
}

@BindingAdapter("imgUrl")
fun bindImage(imgView : ImageView, forecast : Forecast?){

    forecast?.let{
        val imageUrl = "http://openweathermap.org/img/wn/${forecast.weatherType[0].typeIcon}@2x.png"
        val imageUri = imageUrl.toUri().buildUpon().scheme("http").build()

        Glide.with(imgView.context)
            .load(imageUri)
            .apply(RequestOptions().placeholder(R.drawable.ic_baseline_cached_24).error(R.drawable.ic_search_black_24dp))
            .into(imgView)
    }
}





