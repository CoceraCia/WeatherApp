package com.coceracia.weatherapp.ui.main

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coceracia.weatherapp.R
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var actuHour: Runnable

    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.todayData.collect { data ->
                        setTodayValues()
                    }
                }
                launch {
                    viewModel.dailyData.collect { data ->
                        setDailyValues()
                    }
                }
                launch {
                    viewModel.error.collect { e ->
                        if (e == "Exception") {
                            AlertDialog.Builder(this@MainActivity).setTitle("ERROR")
                                .setMessage("Unable to complete the request. Please check your internet connection and try again.")
                                .setPositiveButton("Exit") { _, _ ->
                                    finish()
                                }.setCancelable(false).show()
                        } else if (e == "CertPathValidatorException") {
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("ERROR")
                                .setMessage("A security certificate problem occurred while connecting to the server. Please check your connection or try again later.")
                                .setPositiveButton("Exit") { _, _ ->
                                    finish()
                                }.setCancelable(false).show()
                        }else if (e == "Token") {
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("ERROR")
                                .setMessage("There was an error accessing the API, check that the Token/API key is right")
                                .setPositiveButton("Exit") { _, _ ->
                                    finish()
                                }.setCancelable(false).show()
                        }
                    }
                }
            }
        }

        viewModel.loadWeather()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(actuHour)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setTodayValues() {
        val api = viewModel.api
        val data = api.todayApiData
        val loc = findViewById<TextView>(R.id.tvMainLocation)
        val day = findViewById<TextView>(R.id.tvMainDay)
        val hour = findViewById<TextView>(R.id.tvMainHour)
        val imgState = findViewById<ImageView>(R.id.ivMainWeatherStatus)
        val temp = findViewById<TextView>(R.id.tvActualTemp)
        val welcoming = findViewById<TextView>(R.id.tvMainWelcoming)
        val username = findViewById<TextView>(R.id.tvMainUserName)
        val message = findViewById<TextView>(R.id.tvMessageInfo)

        loc.text = api.location
        day.text = LocalDateTime.now().dayOfWeek.toString()

        val handler = Handler(Looper.getMainLooper())
        actuHour = object : Runnable {
            override fun run() {
                hour.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm a"))
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(actuHour)
        temp.text = data?.main?.temp?.roundToInt().toString() + "ºC"

        message.text = viewModel.getStatusText(data?.weather[0]?.main.toString())
        val img = viewModel.getStatusImage(data?.weather[0]?.main.toString())

        imgState.setImageResource(img)

        welcoming.text = "GOOD ${viewModel.getStateDay()}"
        username.text = "MIGUEL"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDailyValues() {
        val recyclerViewer = findViewById<RecyclerView>(R.id.rvMainDailyWeather)
        val dayList = viewModel.formatedDailyList()

        recyclerViewer.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewer.adapter = WeatherAdapter(dayList)
    }
}

