package com.example.levelupgamer.data.repository

import com.example.levelupgamer.data.model.Product
import com.example.levelupgamer.data.remote.ApiService


class ProductRepository(private val apiService: ApiService) {
    suspend fun getProducts(): List<Product> {
        return listOf(
            Product("JM001", "Catan", "Un clásico juego de estrategia...", 29990, 20, "juegos", "https://dojiw2m9tvv09.cloudfront.net/10102/product/X_catan9477.jpg?43"),
            Product("AC001", "Controlador Xbox", "Experiencia de juego cómoda...", 59990, 25, "accesorios", "https://http2.mlstatic.com/D_NQ_NP_851883-MLA54692335944_032023-O.webp"),
            Product("AC002", "Auriculares HyperX", "Sonido envolvente...", 79990, 10, "accesorios", "https://http2.mlstatic.com/D_NQ_NP_719345-MLU77945147420_082024-O.webp"),
            Product("CO001", "PlayStation 5", "La consola de última generación...", 549990, 5, "consolas", "https://http2.mlstatic.com/D_Q_NP_883946-MLA79964406701_102024-O.webp"),
            Product("MS001", "Mouse Logitech G502", "Con sensor de alta precisión...", 49990, 30, "accesorios", "https://http2.mlstatic.com/D_NQ_NP_657872-MLU70840166924_082023-O.webp")
        )
    }
}