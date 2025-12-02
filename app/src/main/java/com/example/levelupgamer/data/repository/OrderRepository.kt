package com.example.levelupgamer.data.repository

import android.util.Log
import com.example.levelupgamer.data.local.OrderDao
import com.example.levelupgamer.data.model.*
import com.example.levelupgamer.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OrderRepository(
    private val orderDao: OrderDao,
    private val apiService: ApiService
) {

    fun getOrderById(orderId: Long): Flow<OrderWithItems?> {
        return orderDao.getOrderById(orderId)
    }

    suspend fun placeOrder(
        user: User?, // Puede ser null si es invitado
        checkoutState: com.example.levelupgamer.ui.screens.checkout.CheckoutUiState,
        cartItems: List<CartItem>,
        total: Int
    ): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Preparar Entities para Room
                val orderEntity = OrderEntity(
                    userEmail = user?.email ?: checkoutState.email, // Si es invitado usamos el email del form
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

                // 2. Guardar en Room (Local)
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

                // 3. Intentar enviar a la API (Online)
                try {
                    val apiRequest = CreateOrderRequest(
                        customer = CustomerInfo(
                            name = checkoutState.name,
                            lastName = checkoutState.lastName,
                            rut = checkoutState.rut,
                            email = checkoutState.email,
                            phone = checkoutState.phone
                        ),
                        delivery = DeliveryInfo(
                            method = checkoutState.deliveryMethod.name,
                            region = checkoutState.region.takeIf { it.isNotEmpty() },
                            commune = checkoutState.commune.takeIf { it.isNotEmpty() },
                            address = "${checkoutState.street} ${checkoutState.houseNumber} ${checkoutState.apartment}"
                        ),
                        payment = PaymentInfo(method = checkoutState.paymentMethod.name),
                        items = cartItems.map { 
                            OrderItemRequest(it.codigo, it.quantity, it.precio) 
                        },
                        total = total
                    )

                    // Llamada a la API
                    // Nota: Si la URL es falsa o no hay internet, esto lanzará excepción
                    // En un escenario real, descomentar la siguiente línea:
                    // val response = apiService.createOrder(apiRequest)
                    
                    // Si es exitoso:
                    // if (response.success) {
                    //     orderDao.markOrderAsSynced(orderId)
                    // }
                    
                    // Simulamos éxito de API para el demo si no falla antes
                     orderDao.markOrderAsSynced(orderId)

                } catch (e: Exception) {
                    // Falló la API, pero ya guardamos localmente.
                    // El worker de sincronización se encargará después.
                    Log.e("OrderRepository", "Error enviando orden a API: ${e.message}")
                }

                Result.success(orderId)
            } catch (e: Exception) {
                Log.e("OrderRepository", "Error guardando orden: ${e.message}")
                Result.failure(e)
            }
        }
    }
}