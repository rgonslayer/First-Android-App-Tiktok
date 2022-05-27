package com.rgonslayer.tiktokyc.firstapp.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

/**
 * Data class where it holds information on how to serialize / deserialize our data
 *
 * Try deserializing other fields!
 *
 * Note: @field is important!
 */
data class Weather(
    //@field:Json(name = "area_metadata") val areaMetadata: List<AreaMetadata>,
    //@field:Json(name = "items") val forecastItems: List<ForecastItem>
    @SerializedName("area_metadata")
     var areaMetadata: List<AreaMetadata?>? = null,

    @SerializedName("items")
 val forecastItems: List<ForecastItem>? = null
)

data class AirTemperature(
    @field:SerializedName("metadata") val tempMetadata: List<TempMetadata>,
    @field:SerializedName("items") val tempItems: List<TempItem>
)
data class Rainfall(
    @field:SerializedName("metadata") val rainMetadata: List<RainMetadata>,
    @field:SerializedName("items") val tempItems: List<RainItem>
)
data class RelativeHumidity(
    @field:SerializedName("metadata") val HumidMetadata: List<HumidMetadata>,
    @field:SerializedName("items") val tempItems: List<HumidItem>
)
data class WindDirection(
    @field:SerializedName("metadata") val WindMetadata: List<WindMetadata>,
    @field:SerializedName("items") val tempItems: List<WindItem>
)
data class WindSpeed(
    @field:SerializedName("metadata") val SpeedMetadata: List<SpeedMetadata>,
    @field:SerializedName("items") val tempItems: List<SpeedItem>
)
data class UVIndex(
    @field:SerializedName("items") val UVItems: List<UVItem>
)

interface WeatherAPI {
    @GET("v1/environment/2-hour-weather-forecast")
    fun getWeatherData(): Call<Weather>

    @GET("v1/environment/air-temperature")
    fun getAirTemperatureData(): Call<AirTemperature>

    @GET("v1/environment/rainfall")
    fun getRainfallData(): Call<Rainfall>

    @GET("v1/environment/relative-humidity")
    fun getRelativeHumidityData(): Call<RelativeHumidity>

    @GET("v1/environment/wind-direction")
    fun getWindDirectionData(): Call<WindDirection>

    @GET("v1/environment/wind-speed")
    fun getWindSpeedData(): Call<WindSpeed>

    @GET("v1/environment/uv-index")
    fun getUVIndexData(): Call<UVIndex>

}

// Nested class AreaMetadata
class AreaMetadata(name: String, latLong: LatLong) {
    val name: String

    @SerializedName("label_location")
    val latLong: LatLong

    // Nested class LatLong
    inner class LatLong(val latitude: Double, val longitude: Double)

    init {
        this.name = name
        this.latLong = latLong
    }
}

//Nested class ForecastItem
class ForecastItem(
    @field:SerializedName("update_timestamp") val updateTimestamp: String, @field:SerializedName(
        "timestamp"
    ) val timestamp: String, val forecasts: List<AreaForecast>
) {

    // Nested class AreaForecast
    inner class AreaForecast(val name: String, val forecast: String)
}

//Nested class ForecastItem
class UVItem(
    @field:SerializedName("update_timestamp") val updateTimestamp: String, @field:SerializedName(
        "timestamp"
    ) val timestamp: String, @field:SerializedName("index")val forecasts: List<Index>
) {

    // Nested class AreaForecast
    inner class Index(val timeStamp: String, val index: Double)
}

// Nested class AreaMetadata
class TempMetadata(stations: tempValues) {

    @field:SerializedName("stations")
    val stations: tempValues

    inner class tempValues(val id: String, val device_id: String, val name: String, @field:SerializedName("location") val latLong: LatLong) {

    // Nested class LatLong
    inner class LatLong(val latitude: Double, val longitude: Double)}

    init {
        this.stations = stations
    }
}
//Nested class ForecastItem
class TempItem(@field:SerializedName(
        "timestamp"
    ) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}

// Nested class AreaMetadata
class RainMetadata(stations: tempValues) {

    @field:SerializedName("stations")
    val stations: tempValues

    inner class tempValues(val id: String, val device_id: String, val name: String, @field:SerializedName("location") val latLong: LatLong) {

        // Nested class LatLong
        inner class LatLong(val latitude: Double, val longitude: Double)}

    init {
        this.stations = stations
    }
}
//Nested class ForecastItem
class RainItem(@field:SerializedName(
    "timestamp"
) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}

// Nested class AreaMetadata
class WindMetadata(stations: tempValues) {

    @field:SerializedName("stations")
    val stations: tempValues

    inner class tempValues(val id: String, val device_id: String, val name: String, @field:SerializedName("location") val latLong: LatLong) {

        // Nested class LatLong
        inner class LatLong(val latitude: Double, val longitude: Double)}

    init {
        this.stations = stations
    }
}
//Nested class ForecastItem
class WindItem(@field:SerializedName(
   "timestamp"
) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}

class HumidMetadata(stations: tempValues) {

    @field:SerializedName("stations")
    val stations: tempValues

    inner class tempValues(val id: String, val device_id: String, val name: String, @field:SerializedName("location") val latLong: LatLong) {

        // Nested class LatLong
        inner class LatLong(val latitude: Double, val longitude: Double)}

    init {
        this.stations = stations
    }
}
//Nested class ForecastItem
class HumidItem(@field:SerializedName(
    "timestamp"
) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}

// Nested class AreaMetadata
class SpeedMetadata(stations: tempValues) {

    @field:SerializedName("stations")
    val stations: tempValues

    inner class tempValues(val id: String, val device_id: String, val name: String, @field:SerializedName("location") val latLong: LatLong) {

        // Nested class LatLong
        inner class LatLong(val latitude: Double, val longitude: Double)}

    init {
        this.stations = stations
    }
}
//Nested class ForecastItem
class SpeedItem(@field:SerializedName(
   "timestamp"
) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}



