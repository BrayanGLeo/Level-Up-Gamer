package com.example.levelupgamer.data.remote

import com.example.levelupgamer.data.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/chalalo1533/ServicioRest/refs/heads/master/"

interface ApiService {
    @GET("productos.json")
    suspend fun getProducts(): List<Product>
}

object RetrofitClient {
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}