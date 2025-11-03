package com.example.levelupgamer.data.repository

import com.example.levelupgamer.data.local.CartDao
import com.example.levelupgamer.data.model.CartItem
import com.example.levelupgamer.data.model.Product
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    val cartItems: Flow<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun addToCart(product: Product) {
        val existingItem = cartDao.getItem(product.codigo)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            cartDao.updateItem(updatedItem)
        } else {
            val newItem = CartItem(
                codigo = product.codigo,
                nombre = product.nombre,
                precio = product.precio,
                imagen = product.imagen,
                quantity = 1
            )
            cartDao.insertItem(newItem)
        }
    }

    suspend fun updateQuantity(codigo: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            cartDao.deleteItem(codigo)
        } else {
            val item = cartDao.getItem(codigo)
            if (item != null) {
                cartDao.updateItem(item.copy(quantity = newQuantity))
            }
        }
    }

    suspend fun removeFromCart(codigo: String) {
        cartDao.deleteItem(codigo)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}