package com.example.connectionapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/ejemplo2")
    fun getPlatillos(): Call<List<Platillo>>

    @POST("api/enviarPlatillos")
    fun enviarPlatillos(@Body platillosNombres: List<String>): Call<Void>
}
