package com.example.levelupgamer.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val orderId: Long = 0,
    val userEmail: String, // Para vincular con usuario registrado (puede ser null o guest)
    val userName: String,
    val userLastName: String,
    val userRut: String,
    val userPhone: String,
    
    // Delivery Info
    val deliveryMethod: String, // "STORE_PICKUP" or "HOME_DELIVERY"
    val region: String?,
    val commune: String?,
    val addressStreet: String?,
    val addressNumber: String?,
    val addressApartment: String?,
    
    // Payment Info
    val paymentMethod: String, // "WEBPAY" or "TRANSFER"
    val totalAmount: Int,
    val date: Long,
    
    // Sync Status
    val isSynced: Boolean = false // True si ya se envió a la API
)

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val productCode: String,
    val productName: String,
    val productPrice: Int,
    val quantity: Int
)

// Data class para relacion 1 a muchos (para consultas)
data class OrderWithItems(
    @androidx.room.Embedded val order: OrderEntity,
    @androidx.room.Relation(
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>
)