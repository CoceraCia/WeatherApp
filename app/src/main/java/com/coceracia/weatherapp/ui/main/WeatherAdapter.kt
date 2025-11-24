package com.coceracia.weatherapp.ui.main

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.coceracia.weatherapp.R
import com.coceracia.weatherapp.data.model.ForecastItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class WeatherAdapter(private val WeatherList: List<ForecastItem>): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    class WeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imgStatus = itemView.findViewById<ImageView>(R.id.ivRecyclerWeatherStatusTime)
        val tempMin = itemView.findViewById<TextView>(R.id.tvRecyclerTempTimeMin)
        val tempMax = itemView.findViewById<TextView>(R.id.tvRecyclerTempTimeMax)
        val date = itemView.findViewById<TextView>(R.id.tvRecyclerDate)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dailyweather, parent, false)
        return WeatherViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = WeatherList[position]
        var img = R.drawable.cloudy

        when (weather.weather[0].main) {
                "Thunderstorm" -> {
                    img = R.drawable.storm
                }
                "Drizzle" -> {
                    img = R.drawable.drizzle
                }
                "Rain" -> {
                    img = R.drawable.rain
                }
                "Snow" -> {
                    img = R.drawable.snow
                }
                "Clear" -> {
                    img = R.drawable.clear
                }
                "Clouds" -> {
                    img = R.drawable.cloudy
                }

        }

        holder.imgStatus.setImageResource(img)
        holder.tempMin.text = weather?.main?.temp_min?.roundToInt().toString() + "ºC"
        holder.tempMax.text = weather?.main?.temp_max?.roundToInt().toString() + "ºC"
        val time = LocalDateTime.parse(weather.dt_txt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        holder.date.text = time.dayOfWeek.toString().substring(0,3)
    }

    override fun getItemCount(): Int {
        return WeatherList.size
    }
}