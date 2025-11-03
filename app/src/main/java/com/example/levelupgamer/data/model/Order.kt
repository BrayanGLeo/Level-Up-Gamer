package com.example.levelupgamer.data.model

data class Order(
    val items: List<CartItem>,
    val total: Int,
    val customerEmail: String
)

data class OrderResponse(
    val success: Boolean,
    val orderId: String
)