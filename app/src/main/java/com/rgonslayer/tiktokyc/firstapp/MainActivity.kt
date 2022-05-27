package com.rgonslayer.tiktokyc.firstapp


import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rgonslayer.tiktokyc.firstapp.network.*
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import retrofit2.Call
import retrofit2.Callback;
import retrofit2.Response;



import org.jetbrains.annotations.Nullable;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import retrofit2.Retrofit;


class MainActivity : AppCompatActivity() {
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var coordTextView: TextView? = null
    var currLat = 0.0
    var currLong = 0.0
    var areaDataList: List<AreaData>? = null

    private val weather: WeatherAPI = NetworkModule.weatherAPI
    private val temperature: AirTemperature = NetworkModule.TempAPI
    private val humidity: RelativeHumidity = NetworkModule.HumidAPI
    private val direction: WindDirection = NetworkModule.DirectionAPI
    private val speed: WindSpeed = NetworkModule.SpeedAPI
    private val rainfall: Rainfall = NetworkModule.RainfallAPI
    private val uvIndex: UVIndex = NetworkModule.UVIndexAPI


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binds activity layout
        this.setContentView(R.layout.activity_main)
        fusedLocationProviderClient = FusedLocationProviderClient(this);

        sync();

    }

    private fun getLocation() {
        // when permission denied
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
            coordTextView!!.text = "Permission Denied"
            return
        }
        fusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->
            // initialize location
            val location = task.result
            if (location != null) {
                try {
                    // Initialize geoCoder
                    val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                    // Initialize address list
                    val addresses = geocoder.getFromLocation(
                        location.latitude, location.longitude, 1
                    )
                    // Set latitude on coordTextView
                    currLat = addresses[0].latitude
                    currLong = addresses[0].longitude
                    var content = String()
                    content += "$currLat, $currLong"
                    coordTextView!!.text = content
                } catch (e: Exception) {
                    coordTextView!!.text = "Error"
                    e.printStackTrace()
                }
            }
        }
    }

    fun sync(view: View?) {
        sync()
    }

    fun sync() {
        // update currLat and currLong
        getLocation()
        val textView = findViewById<TextView>(R.id.textView)
        val textView2 = findViewById<TextView>(R.id.textView2)
        val textView3 = findViewById<TextView>(R.id.textView3)


        // convert JSON to Java class ApiData
        val call: Call<Weather> = NetworkModule.weatherAPI.getWeatherData()

        call.enqueue(object : Callback<Weather?> {
            override fun onResponse(call: Call<Weather?>?, response: Response<Weather?>) {
                if (!response.isSuccessful()) {
                    // if not successful, return response code
                    textView.text = "Code: " + response.code()
                    return
                }
                val apiData: Weather? = response.body()

                // loop to update areaDataList
                val areaDataList = ArrayList<AreaData>()
                val numOfAreas: Int = apiData?.areaMetadata!!.size // number of areas in SG
                for (i in 0 until numOfAreas) {
                    val currAreaMetadata: AreaMetadata = apiData.areaMetadata[i]
                    val currAreaName: String = currAreaMetadata.name
                    val currLatLong: AreaMetadata.LatLong = currAreaMetadata.latLong
                    val currAreaLat: Double = currLatLong.latitude
                    val currAreaLong: Double = currLatLong.longitude
                    val currAreaForecast: String =
                        apiData.forecastItems.get(0).forecasts.get(i).forecast
                    val currAreaData = AreaData(
                        currAreaName, currAreaLat,
                        currAreaLong, currAreaForecast
                    )
                    areaDataList.add(i, currAreaData)
                }

                // index of nearest area in List<AreaData>
                var nearestAreaIndex = 0

                // function to calculate squared distance
                class Helper {
                    fun getSquaredDistanceFromIndex(index: Int): Double {
                        // scale to prevent rounding-off
                        val scale = 1000.0
                        return Math.pow(
                            areaDataList.get(index).latitude * scale - currLat * scale,
                            2.0
                        ) +
                                Math.pow(
                                    areaDataList.get(index).longitude * scale - currLong * scale,
                                    2.0
                                )
                    }
                }

                // find the nearest area by looping through the list
                val helper = Helper()
                val minSquaredDistance = helper.getSquaredDistanceFromIndex(0)
                for (i in 1 until numOfAreas) {
                    val currSquaredDistance = helper.getSquaredDistanceFromIndex(i)
                    if (currSquaredDistance < minSquaredDistance) {
                        nearestAreaIndex = i
                    }
                }

                // set text here
                var content = ""
                var name = ""
                var forecast = ""
                val shownArea = areaDataList.get(nearestAreaIndex)
                name += """
                 ${shownArea.name}
                 
                 """.trimIndent()
                forecast += """${shownArea.forecast}
"""
                content += shownArea.latitude.toString() + ", " + shownArea.longitude
                textView.text = content
                textView2.text = name
                textView3.text = forecast
            }

            override fun onFailure(call: Call<Weather?>?, t: Throwable) {
                // get error message if there is error
                textView.text = t.message
            }
        })
    }

}

