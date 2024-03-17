package com.example.connectionapi

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/ejemplo2")
    fun getMensaje(): Call<String>
}
