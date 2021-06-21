package com.example.assessmentapplication

import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object SpotifyServiceBuilder {
    private const val url ="https://api.spotify.com/"
    private val okHttp = OkHttpClient.Builder().protocols(Collections.singletonList(Protocol.HTTP_1_1))
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())
        .build()

    fun <T> buildService (serviceType :Class<T>):T{
        return retrofit.create(serviceType)
    }
}