package com.rgonslayer.tiktokyc.firstapp.network

import com.squareup.moshi.Json
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Singleton so we do not recreate multiple instances
 *
 * Network code are expensive so we try to keep a minimal instance of them
 */
object NetworkModule {
    // Create and config an instance of retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.data.gov.sg/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    // Create our API
    val weatherAPI: WeatherAPI = retrofit.create(WeatherAPI::class.java)
    val TempAPI: AirTemperature = retrofit.create(AirTemperature::class.java)
    val HumidAPI: RelativeHumidity = retrofit.create(RelativeHumidity::class.java)
    val DirectionAPI: WindDirection = retrofit.create(WindDirection::class.java)
    val SpeedAPI: WindSpeed = retrofit.create(WindSpeed::class.java)
    val RainfallAPI: Rainfall = retrofit.create(Rainfall::class.java)
    val UVIndexAPI: UVIndex = retrofit.create(UVIndex::class.java)
}
