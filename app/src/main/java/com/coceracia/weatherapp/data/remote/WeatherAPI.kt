package com.coceracia.weatherapp.data.remote

import android.util.Log
import com.coceracia.weatherapp.data.model.Forecast5Response
import com.coceracia.weatherapp.data.model.WeatherData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPI{
    private val BASE_URL = "https://api.openweathermap.org/"

    //Where the token / API key should be
    private val TOKEN = "YOUR_API_KEY"

    //Set up retrofit Builder, setup baseUrl, Transform json to class, build
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
        GsonConverterFactory.create()).build()
    // .client(getUnsafeOkHttpClient())
    private val api = retrofit.create(InterfaceWeatherAPI::class.java)

    //PUBLIC VARIABLES
    val location:String = "Barcelona"
    var todayApiData: WeatherData? = null
    var daily5Data: Forecast5Response? = null

    suspend fun obtainTodayData(): WeatherData {

        val data = this.api.getWeather(lat = 41.3888, lon = 2.1590, apiKey = this.TOKEN)

        Log.d("API", "✅ $data")

        this.todayApiData = data

        return data
    }

    suspend fun obtain5DaysData(): Forecast5Response {
        val data = this.api.getForecast5(lat = 41.3888, lon = 2.1590, apiKey = this.TOKEN)

        Log.d("API", "✅ $data")

        this.daily5Data = data

        return data
    }
}
