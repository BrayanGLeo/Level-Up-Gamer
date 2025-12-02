package com.example.levelupgamer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.levelupgamer.data.model.OrderEntity
import com.example.levelupgamer.data.model.OrderItemEntity
import com.example.levelupgamer.data.model.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Transaction
    @Query("SELECT * FROM orders ORDER BY date DESC")
    fun getAllOrders(): Flow<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders WHERE userEmail = :email ORDER BY date DESC")
    fun getOrdersByUser(email: String): Flow<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    fun getOrderById(orderId: Long): Flow<OrderWithItems?>

    @Transaction
    @Query("SELECT * FROM orders WHERE isSynced = 0")
    suspend fun getUnsyncedOrders(): List<OrderWithItems>

    @Query("UPDATE orders SET isSynced = 1 WHERE orderId = :orderId")
    suspend fun markOrderAsSynced(orderId: Long)
}