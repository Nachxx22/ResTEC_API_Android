package com.example.connectionapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/platillos")
    fun getPlatillos(): Call<List<Platillo>>
    @GET("api/pedidos")
    fun getPedidos(): Call<List<Pedido>>

    @POST("api/platillos2") // Ruta para enviar los platillos al backend
    fun sendPlatillos(@Body platillos: List<Platillo>): Call<Void>
}
