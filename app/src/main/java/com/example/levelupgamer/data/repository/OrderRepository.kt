package com.example.levelupgamer.data.repository

import android.content.Context
import android.util.Log
import com.example.levelupgamer.data.local.OrderDao
import com.example.levelupgamer.data.model.*
import com.example.levelupgamer.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OrderRepository(
    private val orderDao: OrderDao,
    private val apiService: ApiService,
    private val context: Context
) {

    fun getOrderById(orderId: Long): Flow<OrderWithItems?> {
        return orderDao.getOrderById(orderId)
    }

    suspend fun placeOrder(
        user: User?,
        checkoutState: com.example.levelupgamer.ui.screens.checkout.CheckoutUiState,
        cartItems: List<CartItem>,
        total: Int
    ): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val orderEntity = OrderEntity(
                    userEmail = user?.email ?: checkoutState.email,
                    userName = checkoutState.name,
                    userLastName = checkoutState.lastName,
                    userRut = checkoutState.rut,
                    userPhone = checkoutState.phone,
                    deliveryMethod = checkoutState.deliveryMethod.name,
                    region = if (checkoutState.deliveryMethod.name == "HOME_DELIVERY") checkoutState.region else null,
                    commune = if (checkoutState.deliveryMethod.name == "HOME_DELIVERY") checkoutState.commune else null,
                    addressStreet = if (checkoutState.deliveryMethod.name == "HOME_DELIVERY") checkoutState.street else null,
                    addressNumber = if (checkoutState.deliveryMethod.name == "HOME_DELIVERY") checkoutState.houseNumber else null,
                    addressApartment = if (checkoutState.deliveryMethod.name == "HOME_DELIVERY") checkoutState.apartment else null,
                    paymentMethod = checkoutState.paymentMethod.name,
                    totalAmount = total,
                    date = System.currentTimeMillis(),
                    isSynced = false
                )

                val orderId = orderDao.insertOrder(orderEntity)

                val orderItemsEntities = cartItems.map { item ->
                    OrderItemEntity(
                        orderId = orderId,
                        productCode = item.codigo,
                        productName = item.nombre,
                        productPrice = item.precio,
                        quantity = item.quantity
                    )
                }
                orderDao.insertOrderItems(orderItemsEntities)

                Result.success(orderId)

            } catch (e: Exception) {
                Log.e("OrderRepository", "Error guardando orden: ${e.message}")
                Result.failure(e)
            }
        }
    }
}