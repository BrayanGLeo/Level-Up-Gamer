package com.example.levelupgamer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val codigo: String,
    val nombre: String,
    val precio: Int,
    val imagen: String,
    var quantity: Int
)