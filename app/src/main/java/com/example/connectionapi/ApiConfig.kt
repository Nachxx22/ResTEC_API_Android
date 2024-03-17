package com.example.connectionapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiConfig {
    //"http://localhost:5276/"
    private const val BASE_URL = "http://192.168.18.11:5276/" //URL de la API para la PC en la misma red, usar ipconfig en CMD y utilizar el IPv4

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //.addConverterFactory(ScalarsConverterFactory.create()) //para texto plano
         .addConverterFactory(GsonConverterFactory.create()) //JSON
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
