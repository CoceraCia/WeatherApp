package com.coceracia.weatherapp.ui.main

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coceracia.weatherapp.R
import com.coceracia.weatherapp.data.model.Forecast5Response
import com.coceracia.weatherapp.data.model.ForecastItem
import com.coceracia.weatherapp.data.model.WeatherData
import com.coceracia.weatherapp.data.remote.WeatherAPI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.security.cert.CertPathValidatorException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    private val _todayData = MutableSharedFlow<WeatherData>()
    val todayData = _todayData.asSharedFlow()

    private val _dailyData = MutableSharedFlow<Forecast5Response>()
    val dailyData = _dailyData.asSharedFlow()

    val api = WeatherAPI()


    fun loadWeather() {
        viewModelScope.launch {
            try {
                _todayData.emit(api.obtainTodayData())
                _dailyData.emit(api.obtain5DaysData())
            } catch (e: CertPathValidatorException) {
                _error.emit("CertPathValidatorException")
            } catch (e: retrofit2.HttpException) {
                if (e.message?.contains("401") == true) {
                    _error.emit("Token")
                }
            }
            catch (e: Exception) {
                Log.d("error", "$e")
                _error.emit("Exception")
            }
        }
    }

    fun getStatusImage(status: String): Int {
        var img: Int = R.drawable.drizzle
        when (status) {
            "Thunderstorm" -> img = R.drawable.storm
            "Drizzle" -> img = R.drawable.drizzle
            "Rain" -> img = R.drawable.rain
            "Snow" -> img = R.drawable.snow
            "Clear" -> img = R.drawable.clear
            "Clouds" -> img = R.drawable.cloudy
        }
        return img
    }

    fun getStatusText(status:String):String {
        var m:String = "Drizzle"
        when (status) {
            "Thunderstorm" ->
            m = "NO STORM LASTS FOREVER."

            "Drizzle" ->
            m = "A LITTLE RAIN EACH DAY WILL FILL THE RIVERS"

            "Rain" ->
            m = "YOU CAN’T HAVE A RAINBOW WITHOUT A LITTLE RAIN"

            "Snow" ->
            m = "TO APPRECIATE THE BEAUTY OF A SNOWFLAKE, IT IS NECESSARY TO STAND OUT IN THE COLD"

            "Clear" ->
            m = "THE SUN ALWAYS SHINES ABOVE THE CLOUDS"

            "Clouds" ->
            m = "EVERY CLOUD HAS A SILVER LINING"
        }
        return m
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStateDay(): String{
        val now = LocalDateTime.now().hour
        val st = when (now) {
            in 5..11-> "MORNING"
            in 12..17-> "AFTERNOON"
            in 18..21-> "EVENING"
            else -> "NIGHT"
        }
        return st
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatedDailyList(): MutableList<ForecastItem> {
        val dayList = mutableListOf<ForecastItem>()
        api.daily5Data?.list?.forEach { forecastItem ->
            val date = LocalDateTime.parse(
                forecastItem.dt_txt,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )

            var exists = false
            dayList.forEach { dayListit ->
                val fdate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()
                if (dayListit.dt_txt.contains(fdate)) {
                    exists = true
                }
            }
            if (!exists) {
                dayList.add(forecastItem)
            }
        }

        dayList.forEach { day ->
            if (day.dt_txt.contains(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )
            ) {
                day.main.temp_min = api.todayApiData?.main?.temp_min!!
                day.main.temp_max = api.todayApiData?.main?.temp_max!!
            }
            val count = mutableMapOf<String, Int>()
            api.daily5Data?.list?.forEach { day5data ->
                val date = LocalDateTime.parse(
                    day5data.dt_txt,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                )
                if (day.dt_txt.contains(
                        date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()
                    )
                ) {
                    if (day5data.main.temp_min < day.main.temp_min) {
                        day.main.temp_min = day5data.main.temp_min
                    }
                    if (day5data.main.temp_max > day.main.temp_max) {
                        day.main.temp_max = day5data.main.temp_max
                    }
                    if (count[day5data.weather[0].main] == null) {
                        count[day5data.weather[0].main] = 1
                    } else {
                        count[day5data.weather[0].main]?.plus(1)
                    }
                }
            }
            val max = count.maxByOrNull { it.value }
            day.weather[0].main = max!!.key
        }
        return dayList
    }


}