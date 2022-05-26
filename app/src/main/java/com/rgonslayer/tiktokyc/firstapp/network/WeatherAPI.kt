package com.rgonslayer.tiktokyc.firstapp.network

import com.squareup.moshi.Json
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
    @field:Json(name = "area_metadata") val areaMetadata: List<AreaMetadata>,
    @field:Json(name = "items") val forecastItems: List<ForecastItem>
)

data class AirTemperature(
    @field:Json(name = "area_metadata") val tempMetadata: List<TempMetadata>,
    @field:Json(name = "items") val tempItems: List<TempItem>
)
data class Rainfall(
    @field:Json(name = "area_metadata") val rainMetadata: List<RainMetadata>,
    @field:Json(name = "items") val tempItems: List<RainItem>
)
data class RelativeHumidity(
    @field:Json(name = "area_metadata") val HumidMetadata: List<HumidMetadata>,
    @field:Json(name = "items") val tempItems: List<HumidItem>
)
data class WindDirection(
    @field:Json(name = "area_metadata") val WindMetadata: List<WindMetadata>,
    @field:Json(name = "items") val tempItems: List<WindItem>
)
data class WindSpeed(
    @field:Json(name = "area_metadata") val SpeedMetadata: List<SpeedMetadata>,
    @field:Json(name = "items") val tempItems: List<SpeedItem>
)
data class UVIndex(
    @field:Json(name = "items") val UVItems: List<UVItem>
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
class AreaMetadata(val name: String, @field:Json(name = "label_location") val latLong: LatLong) {

    // Nested class LatLong
    inner class LatLong(val latitude: Double, val longitude: Double)
}
//Nested class ForecastItem
class ForecastItem(
    @field:Json(name = "update_timestamp") val updateTimestamp: String, @field:Json(
        name = "timestamp"
    ) val timestamp: String, val forecasts: List<AreaForecast>
) {
    // Nested class AreaForecast
    inner class AreaForecast(val name: String, val forecast: String)
}

//Nested class ForecastItem
class UVItem(
    @field:Json(name = "update_timestamp") val updateTimestamp: String, @field:Json(
        name = "timestamp"
    ) val timestamp: String, val forecasts: List<Index>
) {

    // Nested class AreaForecast
    inner class Index(val timeStamp: String, val index: Double)
}

// Nested class AreaMetadata
class TempMetadata(val name: String, @field:Json(name = "location") val latLong: LatLong) {

    // Nested class LatLong
    inner class LatLong(val latitude: Double, val longitude: Double)
}
//Nested class ForecastItem
class TempItem(@field:Json(
        name = "timestamp"
    ) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}

// Nested class AreaMetadata
class RainMetadata(val name: String, @field:Json(name = "location") val latLong: LatLong) {

    // Nested class LatLong
    inner class LatLong(val latitude: Double, val longitude: Double)
}
//Nested class ForecastItem
class RainItem(@field:Json(
    name = "timestamp"
) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}

// Nested class AreaMetadata
class WindMetadata(val name: String, @field:Json(name = "location") val latLong: LatLong) {

    // Nested class LatLong
    inner class LatLong(val latitude: Double, val longitude: Double)
}
//Nested class ForecastItem
class WindItem(@field:Json(
    name = "timestamp"
) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}

// Nested class AreaMetadata
class HumidMetadata(val name: String, @field:Json(name = "location") val latLong: LatLong) {

    // Nested class LatLong
    inner class LatLong(val latitude: Double, val longitude: Double)
}
//Nested class ForecastItem
class HumidItem(@field:Json(
    name = "timestamp"
) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}

// Nested class AreaMetadata
class SpeedMetadata(val name: String, @field:Json(name = "location") val latLong: LatLong) {

    // Nested class LatLong
    inner class LatLong(val latitude: Double, val longitude: Double)
}
//Nested class ForecastItem
class SpeedItem(@field:Json(
    name = "timestamp"
) val timestamp: String, val forecasts: List<Readings>
) {
    // Nested class AreaForecast
    inner class Readings(val name: String, val temperature: Double)
}



