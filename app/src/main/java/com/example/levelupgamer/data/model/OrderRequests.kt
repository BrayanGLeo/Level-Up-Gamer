package com.example.levelupgamer.data.model

data class CreateOrderRequest(
    val customer: CustomerInfo,
    val delivery: DeliveryInfo,
    val payment: PaymentInfo,
    val items: List<OrderItemRequest>,
    val total: Int
)

data class CustomerInfo(
    val name: String,
    val lastName: String,
    val rut: String,
    val email: String,
    val phone: String
)

data class DeliveryInfo(
    val method: String,
    val region: String?,
    val commune: String?,
    val address: String?
)

data class PaymentInfo(
    val method: String
)

data class OrderItemRequest(
    val productCode: String,
    val quantity: Int,
    val price: Int
)