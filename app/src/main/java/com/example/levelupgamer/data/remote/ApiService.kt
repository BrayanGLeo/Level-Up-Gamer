package com.example.levelupgamer.data.remote

import com.example.levelupgamer.data.model.CreateOrderRequest
import com.example.levelupgamer.data.model.Order
import com.example.levelupgamer.data.model.OrderResponse
import com.example.levelupgamer.data.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Usamos una URL que funcione localmente o un placeholder. 
// Si usas un emulador, 10.0.2.2 es localhost de tu PC.
private const val BASE_URL = "https://tu-api-backend.com/api/" 

interface ApiService {

    @GET("products")
    suspend fun getProducts(): List<Product>

    @POST("sales")
    suspend fun postSale(@Body order: Order): OrderResponse
    
    @POST("orders")
    suspend fun createOrder(@Body order: CreateOrderRequest): OrderResponse
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