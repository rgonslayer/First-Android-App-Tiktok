package com.rgonslayer.tiktokyc.firstapp.network

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
    val WeatherAPI: Weather = retrofit.create(Weather::class.java)
}