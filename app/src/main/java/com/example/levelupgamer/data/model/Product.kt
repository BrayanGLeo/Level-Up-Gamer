package com.example.levelupgamer.data.model

data class Product(
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int,
    val categoria: String,
    val imagen: String
)