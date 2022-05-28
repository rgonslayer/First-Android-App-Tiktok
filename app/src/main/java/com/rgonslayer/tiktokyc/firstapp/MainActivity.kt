package com.rgonslayer.tiktokyc.firstapp


import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.rgonslayer.tiktokyc.firstapp.network.*
import java.util.*
import retrofit2.*


class MainActivity : AppCompatActivity() {
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var coordTextView: TextView? = null
    var currLat = 0.0
    var currLong = 0.0
    var areaDataList: ArrayList<AreaData>? = ArrayList<AreaData>()
    var areaDataList1: ArrayList<AreaData1>? = ArrayList<AreaData1>()
    var areaDataList2: ArrayList<AreaData2>? = ArrayList<AreaData2>()
    var areaDataList3: ArrayList<AreaData3>? = ArrayList<AreaData3>()
    var areaDataList4: ArrayList<AreaData4>? = ArrayList<AreaData4>()
    var areaDataList5: ArrayList<AreaData5>? = ArrayList<AreaData5>()
    var areaDataList6: ArrayList<AreaData6>? = ArrayList<AreaData6>()
    var textView: TextView? = null
    var textView4: TextView? = null
    var textView5: TextView? = null
    var textView7: TextView? = null
    var textView8: TextView? = null
    var textView2: TextView? = null
    var textView9: TextView? = null
    var textView3: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binds activity layout
        this.setContentView(R.layout.activity_main)
        coordTextView = findViewById(R.id.tv_title);
        textView = findViewById(R.id.textView);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView2 = findViewById(R.id.textView2);
        textView9 = findViewById(R.id.textView9);
        textView3 = findViewById(R.id.textView3);
        fusedLocationProviderClient = FusedLocationProviderClient(this);

        sync()
        sync1()
        sync2()
        sync3()
        sync4()
        sync5()
        //sync6()
    }

    fun getLocation() {
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
        fusedLocationProviderClient?.lastLocation?.addOnCompleteListener { task ->
            // initialize location
            fun onComplete(task : Task<Location>) {
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
    }


    fun sync() {
        // update currLat and currLong
        getLocation()

        // convert JSON to Java class ApiData
        val callWeather: Call<Weather> = NetworkModule.weatherAPI.getWeatherData()

        callWeather.enqueue(object : Callback<Weather?> {
            override fun onResponse(call: Call<Weather?>?, response: Response<Weather?>) {
                if (!response.isSuccessful()) {
                    // if not successful, return response code
                    textView!!.text = "Code: " + response.code()
                    return
                }
                val apiData: Weather? = response.body()

                // loop to update areaDataList

                val numOfAreas: Int = apiData!!.areaMetadata!!.size // number of areas in SG
                for (i in 0 until numOfAreas) {
                    val currAreaMetadata: AreaMetadata? = apiData.areaMetadata!![i]
                    val currAreaName: String = currAreaMetadata!!.name
                    val currLatLong: AreaMetadata.LatLong = currAreaMetadata!!.latLong
                    val currAreaLat: Double = currLatLong.latitude
                    val currAreaLong: Double = currLatLong.longitude
                    val currAreaForecast: String =
                        apiData.forecastItems!!.get(0).forecasts.get(i).forecast
                    val currAreaData = AreaData(
                        currAreaName, currAreaLat,
                        currAreaLong, currAreaForecast
                    )
                    areaDataList!!.add(i, currAreaData)
                }

                // index of nearest area in List<AreaData>
                var nearestAreaIndex = 0

                // function to calculate squared distance
                class Helper {
                    fun getSquaredDistanceFromIndex(index: Int): Double {
                        // scale to prevent rounding-off
                        val scale = 1000.0
                        return Math.pow(
                            areaDataList?.get(index)!!.latitude * scale - currLat * scale,
                            2.0
                        ) +
                                Math.pow(
                                    areaDataList!!.get(index).longitude * scale - currLong * scale,
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
                val shownArea = areaDataList!![nearestAreaIndex]
                name += shownArea.name
                forecast += shownArea.forecast
                content += shownArea.latitude.toString() + ", " + shownArea.longitude
                coordTextView!!.setText(name)
                textView!!.setText(content)
                textView4!!.setText(forecast)
            }

            override fun onFailure(call: Call<Weather?>?, t: Throwable) {
                // get error message if there is error

            }
        })

    }
    fun sync1() {
        // convert JSON to Java class ApiData
        val callTemp: Call<AirTemperature> = NetworkModule.weatherAPI.getAirTemperatureData()

        callTemp.enqueue(object : Callback<AirTemperature?> {
            override fun onResponse(call: Call<AirTemperature?>?, response: Response<AirTemperature?>) {
                if (!response.isSuccessful()) {
                    // if not successful, return response code
                    textView!!.text = "Code: " + response.code()
                    return
                }
                val apiData: AirTemperature? = response.body()

                // loop to update areaDataList

                val numOfAreas: Int = apiData!!.tempMetadata!!.size // number of areas in SG
                for (i in 0 until numOfAreas) {
                    val currAreaMetadata: TempMetadata = apiData.tempMetadata!![i]
                    val currStation: TempMetadata.tempValues = currAreaMetadata.stations
                    val currAreaName: String = currStation!!.name
                    val currLatLong: TempMetadata.tempValues.LatLong = currStation!!.latLong
                    val currAreaLat: Double = currLatLong.latitude
                    val currAreaLong: Double = currLatLong.longitude
                    val currAreaForecast: Double =
                        apiData.tempItems!!.get(0).forecasts.get(i).temperature
                    val currAreaData = AreaData1(
                        currAreaName, currAreaLat,
                        currAreaLong, currAreaForecast
                    )
                    areaDataList1!!.add(i, currAreaData)
                }

                // index of nearest area in List<AreaData>
                var nearestAreaIndex = 0

                // function to calculate squared distance
                class Helper {
                    fun getSquaredDistanceFromIndex(index: Int): Double {
                        // scale to prevent rounding-off
                        val scale = 1000.0
                        return Math.pow(
                            areaDataList1?.get(index)!!.latitude * scale - currLat * scale,
                            2.0
                        ) +
                                Math.pow(
                                    areaDataList1!!.get(index).longitude * scale - currLong * scale,
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
                var airTemp = ""
                val shownArea = areaDataList1!![nearestAreaIndex]
                airTemp += shownArea.airTemp
                textView5!!.setText(airTemp)
            }

            override fun onFailure(call: Call<AirTemperature?>, t: Throwable) {
                // get error message if there is error

            }
        })
    }
    fun sync2() {
        // convert JSON to Java class ApiData
        val callTemp: Call<Rainfall> = NetworkModule.weatherAPI.getRainfallData()

        callTemp.enqueue(object : Callback<Rainfall?> {
            override fun onResponse(call: Call<Rainfall?>?, response: Response<Rainfall?>) {
                if (!response.isSuccessful()) {
                    // if not successful, return response code
                    textView!!.text = "Code: " + response.code()
                    return
                }
                val apiData: Rainfall? = response.body()

                // loop to update areaDataList

                val numOfAreas: Int = apiData!!.rainMetadata!!.size // number of areas in SG
                for (i in 0 until numOfAreas) {
                    val currAreaMetadata: RainMetadata = apiData.rainMetadata!![i]
                    val currStation: RainMetadata.tempValues = currAreaMetadata.stations
                    val currAreaName: String = currStation!!.name
                    val currLatLong: RainMetadata.tempValues.LatLong = currStation!!.latLong
                    val currAreaLat: Double = currLatLong.latitude
                    val currAreaLong: Double = currLatLong.longitude
                    val currAreaForecast: Double =
                        apiData.tempItems!!.get(0).forecasts.get(i).temperature
                    val currAreaData = AreaData2(
                        currAreaName, currAreaLat,
                        currAreaLong, currAreaForecast
                    )
                    areaDataList2!!.add(i, currAreaData)
                }

                // index of nearest area in List<AreaData>
                var nearestAreaIndex = 0

                // function to calculate squared distance
                class Helper {
                    fun getSquaredDistanceFromIndex(index: Int): Double {
                        // scale to prevent rounding-off
                        val scale = 1000.0
                        return Math.pow(
                            areaDataList2?.get(index)!!.latitude * scale - currLat * scale,
                            2.0
                        ) +
                                Math.pow(
                                    areaDataList2!!.get(index).longitude * scale - currLong * scale,
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
                var rainfall = ""
                val shownArea = areaDataList2!![nearestAreaIndex]
                rainfall += shownArea.rainfall
                textView7!!.setText(rainfall)
            }

            override fun onFailure(call: Call<Rainfall?>, t: Throwable) {
                // get error message if there is error

            }
        })
    }
    fun sync3() {
        // convert JSON to Java class ApiData
        val callTemp: Call<RelativeHumidity> = NetworkModule.weatherAPI.getRelativeHumidityData()

        callTemp.enqueue(object : Callback<RelativeHumidity?> {
            override fun onResponse(call: Call<RelativeHumidity?>?, response: Response<RelativeHumidity?>) {
                if (!response.isSuccessful()) {
                    // if not successful, return response code
                    textView!!.text = "Code: " + response.code()
                    return
                }
                val apiData: RelativeHumidity? = response.body()

                // loop to update areaDataList

                val numOfAreas: Int = apiData!!.HumidMetadata!!.size // number of areas in SG
                for (i in 0 until numOfAreas) {
                    val currAreaMetadata: HumidMetadata = apiData.HumidMetadata!![i]
                    val currStation: HumidMetadata.tempValues = currAreaMetadata.stations
                    val currAreaName: String = currStation!!.name
                    val currLatLong: HumidMetadata.tempValues.LatLong = currStation!!.latLong
                    val currAreaLat: Double = currLatLong.latitude
                    val currAreaLong: Double = currLatLong.longitude
                    val currAreaForecast: Double =
                        apiData.tempItems!!.get(0).forecasts.get(i).temperature
                    val currAreaData = AreaData3(
                        currAreaName, currAreaLat,
                        currAreaLong, currAreaForecast
                    )
                    areaDataList3!!.add(i, currAreaData)
                }

                // index of nearest area in List<AreaData>
                var nearestAreaIndex = 0

                // function to calculate squared distance
                class Helper {
                    fun getSquaredDistanceFromIndex(index: Int): Double {
                        // scale to prevent rounding-off
                        val scale = 1000.0
                        return Math.pow(
                            areaDataList3?.get(index)!!.latitude * scale - currLat * scale,
                            2.0
                        ) +
                                Math.pow(
                                    areaDataList3!!.get(index).longitude * scale - currLong * scale,
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
                var humidity = ""
                val shownArea = areaDataList3!![nearestAreaIndex]
                humidity += shownArea.humidity
                textView9!!.setText(humidity)
            }

            override fun onFailure(call: Call<RelativeHumidity?>, t: Throwable) {
                // get error message if there is error

            }
        })
    }
    fun sync4() {
        // convert JSON to Java class ApiData
        val callTemp: Call<WindSpeed> = NetworkModule.weatherAPI.getWindSpeedData()

        callTemp.enqueue(object : Callback<WindSpeed?> {
            override fun onResponse(call: Call<WindSpeed?>?, response: Response<WindSpeed?>) {
                if (!response.isSuccessful()) {
                    // if not successful, return response code
                    textView!!.text = "Code: " + response.code()
                    return
                }
                val apiData: WindSpeed? = response.body()

                // loop to update areaDataList

                val numOfAreas: Int = apiData!!.SpeedMetadata!!.size // number of areas in SG
                for (i in 0 until numOfAreas) {
                    val currAreaMetadata: SpeedMetadata = apiData.SpeedMetadata!![i]
                    val currStation: SpeedMetadata.tempValues = currAreaMetadata.stations
                    val currAreaName: String = currStation!!.name
                    val currLatLong: SpeedMetadata.tempValues.LatLong = currStation!!.latLong
                    val currAreaLat: Double = currLatLong.latitude
                    val currAreaLong: Double = currLatLong.longitude
                    val currAreaForecast: Double =
                        apiData.tempItems!!.get(0).forecasts.get(i).temperature
                    val currAreaData = AreaData4(
                        currAreaName, currAreaLat,
                        currAreaLong, currAreaForecast
                    )
                    areaDataList4!!.add(i, currAreaData)
                }

                // index of nearest area in List<AreaData>
                var nearestAreaIndex = 0

                // function to calculate squared distance
                class Helper {
                    fun getSquaredDistanceFromIndex(index: Int): Double {
                        // scale to prevent rounding-off
                        val scale = 1000.0
                        return Math.pow(
                            areaDataList4?.get(index)!!.latitude * scale - currLat * scale,
                            2.0
                        ) +
                                Math.pow(
                                    areaDataList4!!.get(index).longitude * scale - currLong * scale,
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
                var windSpeed = ""
                val shownArea = areaDataList4!![nearestAreaIndex]
                windSpeed += shownArea.windSpeed
                textView2!!.setText(windSpeed)
            }

            override fun onFailure(call: Call<WindSpeed?>, t: Throwable) {
                // get error message if there is error

            }
        })
    }
    fun sync5() {
        // convert JSON to Java class ApiData
        val callTemp: Call<WindDirection> = NetworkModule.weatherAPI.getWindDirectionData()

        callTemp.enqueue(object : Callback<WindDirection?> {
            override fun onResponse(call: Call<WindDirection?>?, response: Response<WindDirection?>) {
                if (!response.isSuccessful()) {
                    // if not successful, return response code
                    textView!!.text = "Code: " + response.code()
                    return
                }
                val apiData: WindDirection? = response.body()

                // loop to update areaDataList

                val numOfAreas: Int = apiData!!.WindMetadata!!.size // number of areas in SG
                for (i in 0 until numOfAreas) {
                    val currAreaMetadata: WindMetadata = apiData.WindMetadata!![i]
                    val currStation: WindMetadata.tempValues = currAreaMetadata.stations
                    val currAreaName: String = currStation!!.name
                    val currLatLong: WindMetadata.tempValues.LatLong = currStation!!.latLong
                    val currAreaLat: Double = currLatLong.latitude
                    val currAreaLong: Double = currLatLong.longitude
                    val currAreaForecast: Double =
                        apiData.tempItems!!.get(0).forecasts.get(i).temperature
                    val currAreaData = AreaData5(
                        currAreaName, currAreaLat,
                        currAreaLong, currAreaForecast
                    )
                    areaDataList5!!.add(i, currAreaData)
                }

                // index of nearest area in List<AreaData>
                var nearestAreaIndex = 0

                // function to calculate squared distance
                class Helper {
                    fun getSquaredDistanceFromIndex(index: Int): Double {
                        // scale to prevent rounding-off
                        val scale = 1000.0
                        return Math.pow(
                            areaDataList5?.get(index)!!.latitude * scale - currLat * scale,
                            2.0
                        ) +
                                Math.pow(
                                    areaDataList5!!.get(index).longitude * scale - currLong * scale,
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
                var windDirection = ""
                val shownArea = areaDataList5!![nearestAreaIndex]
                windDirection += shownArea.windDirection
                textView5!!.setText(windDirection)
            }

            override fun onFailure(call: Call<WindDirection?>, t: Throwable) {
                // get error message if there is error

            }
        })
    }
    /*fun sync6() {
        // convert JSON to Java class ApiData
        val callTemp: Call<UVIndex> = NetworkModule.weatherAPI.getUVIndexData()

        callTemp.enqueue(object : Callback<UVIndex?> {
            override fun onResponse(call: Call<UVIndex?>?, response: Response<UVIndex?>) {
                if (!response.isSuccessful()) {
                    // if not successful, return response code
                    textView!!.text = "Code: " + response.code()
                    return
                }
                val apiData: UVIndex? = response.body()

                // loop to update areaDataList

                val numOfAreas: Int = apiData!!.UVItems!!.size // number of areas in SG
                val currAreaForecast: Double =
                    apiData.UVItems!!.get(0).forecasts.get(0).index
                val currAreaData = AreaData6(currAreaForecast)
                areaDataList6!!.add(0, currAreaData)


                // set text here
                var UVIndex = ""
                val shownArea = areaDataList6!![0]
                UVIndex += shownArea.UVIndex
                textView3!!.setText(UVIndex)
            }

            override fun onFailure(call: Call<UVIndex?>, t: Throwable) {
                // get error message if there is error
                textView!!.text = t.message
            }
        })
    }*/
}

