package com.example.connectionapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/ejemplo2")
    fun getPlatillos(): Call<List<Platillo>>

    @POST("api/platillos") // Ruta para enviar los platillos al backend
    fun sendPlatillos(@Body platillos: List<Platillo>): Call<Void>
}
