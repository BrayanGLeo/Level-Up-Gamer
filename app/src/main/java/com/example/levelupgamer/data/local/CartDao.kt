package com.example.levelupgamer.data.local

import androidx.room.*
import com.example.levelupgamer.data.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItem)

    @Update
    suspend fun updateItem(item: CartItem)

    @Query("DELETE FROM cart_items WHERE codigo = :codigo")
    suspend fun deleteItem(codigo: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_items WHERE codigo = :codigo LIMIT 1")
    suspend fun getItem(codigo: String): CartItem?
}
