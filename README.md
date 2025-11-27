# 🌤 Simple Weather App
A clean and minimal **Android Weather App** built with **Kotlin**, designed to show real-time weather updates and forecasts from Barcelona in a simple and elegant UI.  
It greets the user personally and provides daily motivation with a positive message.


---

## 📱 Features

- 🌞 **Current Weather Overview** – Displays temperature and weather conditions for Barcelona.  
- 📅 **5-Day Forecast** – Shows the next five days’ high and low temperatures with icons.  
- 👋 **Personalized Greeting** – Dynamically greets the user (e.g., “Good Morning, Miguel”).  
- 🕗 **Real-Time Data** – Updates automatically with your local time and location.  
- 💎 **Minimal Design** – Focused on readability, simplicity, and soft colors.


---

## 🧩 Tech Stack

- **Language:** Kotlin  
- **Architecture:** MVVM (Model–View–ViewModel)  
- **UI:** XML Layout  
- **Networking:** Retrofit + Coroutines 
- **Data Source:** OpenWeatherMap API

---

## 🚀 Setup & Installation
<img height="700px" align="right" alt="imagen" src="https://github.com/user-attachments/assets/c2367932-0f4d-4d03-859a-961779b083d3" />

1. **Clone this repository**
   ```bash
   git clone https://github.com/yourusername/simple-weather-app.git
   cd simple-weather-app
   ```
2. **Add your API key**
   - Go to:
       ```bash
        app/
        └── kotlin+java/
            └── com.coceracia.weatherapp/
                └── data/
                    └── remote/
                        ├── InterfaceWeatherAPI.kt
                        └── WeatherAPI.kt   👈
        ```
   - Add your weather API key, at:
       ```bash
       private val TOKEN = "YOUR_API_KEY_HERE"
       ```
       
3. **Build and Run**
   - Open the project in Android Studio.
   - Select a device or emulator.
   - Click Run ▶️.

## 📽️ Video Demo
[![Demo Short](https://img.youtube.com/vi/dEUotsC0_1Y/maxresdefault.jpg)](https://youtube.com/shorts/dEUotsC0_1Y?si=LCIAFPXqKwg8w7nk)


