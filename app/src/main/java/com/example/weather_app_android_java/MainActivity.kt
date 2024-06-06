package com.example.weather_app_android_java

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import weatherapimodel

class MainActivity : AppCompatActivity() {
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Setup search view listener
        val searchview = findViewById<SearchView>(R.id.searchView)
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                fetchweather(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        // Fetch weather for a default city (e.g., London)
        fetchweather("London")

        // Set location text view click listener
        val locationtextview = findViewById<TextView>(R.id.locationTextView)
        locationtextview.setOnClickListener { view: View? -> fetchLocationAndWeather() }
    }

    private fun fetchLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Fetch the last known location
        fusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val location = task.result
                val latitude = location!!.latitude
                val longitude = location.longitude
                fetchweatherByLocation(latitude, longitude)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Unable to get location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fetchweatherByLocation(latitude: Double, longitude: Double) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()

        val api = retrofit.create(api_interface::class.java)
        val response: Call<weatherapimodel> = api.getWeatherByCoordinates(
            latitude,
            longitude,
            "1af5ffff4e3e1675f012193ef48083de",
            "metric"
        )

        response.enqueue(object : Callback<weatherapimodel?> {
            override fun onResponse(
                call: Call<weatherapimodel?>,
                response: Response<weatherapimodel?>
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    val responseBody: weatherapimodel? = response.body()
                    if (responseBody != null) {
                        updateUI(responseBody)
                    }
                } else {
                    handleErrorResponse()
                }
            }

            override fun onFailure(call: Call<weatherapimodel?>, t: Throwable) {
                handleErrorResponse()
            }

            private fun handleErrorResponse() {
                Log.e("AHMED", "Error fetching weather data")
                Toast.makeText(this@MainActivity, "Unable to fetch weather data", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun fetchweather(city: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()

        val api = retrofit.create(api_interface::class.java)
        val response: Call<weatherapimodel> =
            api.getWeatherByCity(city, "1af5ffff4e3e1675f012193ef48083de", "metric")

        response.enqueue(object : Callback<weatherapimodel?> {
            override fun onResponse(
                call: Call<weatherapimodel?>,
                response: Response<weatherapimodel?>
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    val responseBody: weatherapimodel? = response.body()
                    if (responseBody != null) {
                        updateUI(responseBody)
                    }
                } else {
                    handleErrorResponse()
                }
            }

            override fun onFailure(call: Call<weatherapimodel?>, t: Throwable) {
                handleErrorResponse()
            }

            private fun handleErrorResponse() {
                Log.e("AHMED", "Error fetching weather data")
                Toast.makeText(
                    this@MainActivity,
                    "City not found. Please enter a valid city name.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun updateUI(responseBody: weatherapimodel) {
        val temperature: String = responseBody.main.temp.toString()
        val maxtemp: String = responseBody.main.temp_max.toString()
        val mintemp: String = responseBody.main.temp_min.toString()
        val humidity: String = responseBody.main.humidity.toString()
        val condition: String = responseBody.weather.get(0).main.toString()
        val pressure: String = responseBody.main.pressure.toString()
        val description: String = responseBody.weather.get(0).description.toString()
        val name: String = responseBody.name.toString()

        val temp = findViewById<TextView>(R.id.temperatureTextView)
        val humidityy = findViewById<TextView>(R.id.humidityTextView)
        val conditonss = findViewById<TextView>(R.id.weatherConditionTextView)
        val pressurey = findViewById<TextView>(R.id.seaLevelTextView)
        val maxtemprature = findViewById<TextView>(R.id.maxTempTextView)
        val mintemprature = findViewById<TextView>(R.id.minTempTextView)
        val description_ = findViewById<TextView>(R.id.bigweatherConditionTextView)
        val cityname = findViewById<TextView>(R.id.locationTextView)

        temp.text = "$temperature°C"
        humidityy.text = "$humidity %"
        conditonss.text = condition
        pressurey.text = "$pressure hPa"
        maxtemprature.text = "Max $maxtemp°C"
        mintemprature.text = "Min $mintemp°C"
        description_.text = description
        cityname.text = name

        // Set animation based on weather description
        setanimation(description)
    }

    private fun setanimation(description: String) {
        val animationView = findViewById<LottieAnimationView>(R.id.Animation)
        val background =
            findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)

        when (description.toUpperCase()) {

            "CLEAR", "CLEAR SKY", "CLEAR SCATTERED", "CLEAR BROKEN", "SUNNY" -> {

                animationView.setAnimation(R.raw.sun)
                background.setBackgroundResource(R.drawable.sunny_background)
            }

            "HAZE", "PARTLY CLOUDY", "CLOUDS", "OVERCAST", "FEW CLOUDS", "LIGHT CLOUDS", "OVERCAST CLOUDS", "BROKEN CLOUDS" -> {
                animationView.setAnimation(R.raw.cloud)
                background.setBackgroundResource(R.drawable.colud_background)
            }

            "RAIN", "DRIZZLE", "SHOWERS", "lIGHTRAIN", "THUNDERSTORM" -> {
                animationView.setAnimation(R.raw.rain)


                background.setBackgroundResource(R.drawable.rain_background)

            }


            "Snow", "Heavy Snow", "BLIZZARD", "HAIL" -> {
                animationView.setAnimation(R.raw.snow)
                background.setBackgroundResource(R.drawable.snow_background)

            }

            else -> {
                animationView.setAnimation(R.raw.snow) // A default animation in case description does not match any case
            }
        }

        animationView.playAnimation()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}