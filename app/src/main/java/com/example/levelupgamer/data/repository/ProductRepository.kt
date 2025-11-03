package com.example.levelupgamer.data.repository

import com.example.levelupgamer.data.model.Product
import com.example.levelupgamer.data.remote.ApiService

class ProductRepository(private val apiService: ApiService) {
    suspend fun getProducts(): List<Product> {
        return listOf(
            Product(
                codigo = "JM001",
                nombre = "Catan",
                descripcion = "Un clásico juego de estrategia y gestión de recursos...",
                precio = 29990,
                stock = 20,
                categoria = "juegos",
                imagen = "https://dojiw2m9tvv09.cloudfront.net/10102/product/X_catan9477.jpg?43"
            ),
            Product(
                codigo = "AC001",
                nombre = "Controlador Xbox",
                descripcion = "Experiencia de juego cómoda y precisa...",
                precio = 59990,
                stock = 25,
                categoria = "accesorios",
                imagen = "https://http2.mlstatic.com/D_NQ_NP_851883-MLA54692335944_032023-O.webp"
            ),
            Product(
                codigo = "AC002",
                nombre = "Auriculares HyperX",
                descripcion = "Sonido envolvente 7.1 para inmersión total...",
                precio = 79990,
                stock = 10,
                categoria = "accesorios",
                imagen = "https://http2.mlstatic.com/D_NQ_NP_719345-MLU77945147420_082024-O.webp"
            ),
            Product(
                codigo = "CO001",
                nombre = "PlayStation 5",
                descripcion = "La consola de última generación de Sony...",
                precio = 549990,
                stock = 5,
                categoria = "consolas",
                imagen = "https://static2.pisapapeles.net/uploads/2024/09/GXH7fzoXIAAqLGh.jpeg"
            ),
            Product(
                codigo = "AC003",
                nombre = "Mouse Logitech G502",
                descripcion = "Con sensor de alta precisión y pesos ajustables...",
                precio = 49990,
                stock = 30,
                categoria = "accesorios",
                imagen = "https://http2.mlstatic.com/D_NQ_NP_657872-MLU70840166924_082023-O.webp"
            ),
            Product(
                codigo = "CO002",
                nombre = "Nintendo Switch OLED",
                descripcion = "La versatilidad de Nintendo con una pantalla OLED...",
                precio = 349990,
                stock = 15,
                categoria = "consolas",
                imagen = "https://media.falabella.com/falabellaCL/123426297_01/w=800,h=800,fit=pad"
            ),
            Product(
                codigo = "AC004",
                nombre = "Silla Gamer",
                descripcion = "Silla ergonómica con soporte lumbar...",
                precio = 129990,
                stock = 8,
                categoria = "accesorios",
                imagen = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_eJAGG3mPro93cQDCHvq5yis6rEBVfxar3SGGGO3Huic3NRc6l4pv5gYTEpZXS2N5JaI&usqp=CAU"
            ),
            Product(
                codigo = "HW001",
                nombre = "Tarjeta Gráfica RTX 4070",
                descripcion = "Potencia gráfica de nueva generación...",
                precio = 699990,
                stock = 7,
                categoria = "hardware",
                imagen = "https://www.winpy.cl/files/38199-5092-GeForce-RTX-4070-SUPER-12G-GAMING-X-SLIM-1.jpg"
            ),
            Product(
                codigo = "SW001",
                nombre = "EA Sports FC 24 (PS5)",
                descripcion = "El nuevo simulador de fútbol...",
                precio = 64990,
                stock = 50,
                categoria = "juegos",
                imagen = "https://hnau.imgix.net/media/catalog/product/p/s/ps5_ea_sports_fc_24.jpg"
            ),
            Product(
                codigo = "SW002",
                nombre = "PlayStation Plus 12 Meses",
                descripcion = "Acceso a multijugador online y juegos mensuales...",
                precio = 54990,
                stock = 100,
                categoria = "software",
                imagen = "https://image.api.playstation.com/vulcan/ap/rnd/202206/1610/zAcqUBcDDg6D7JCfiwfEk69e.png"
            ),
            Product(
                codigo = "JM002",
                nombre = "Dungeons & Dragons",
                descripcion = "Set de inicio del popular juego de rol...",
                precio = 34990,
                stock = 15,
                categoria = "juegos",
                imagen = "https://i.blogs.es/dfa33f/d-d-starter-set-heroes-of-the-borderlands--1-/500_333.jpeg"
            ),
            Product(
                codigo = "HW002",
                nombre = "Procesador AMD Ryzen 7",
                descripcion = "CPU de alto rendimiento para gaming y creación...",
                precio = 299990,
                stock = 12,
                categoria = "hardware",
                imagen = "https://cintegral.cl/wp-content/uploads/2024/03/1877348_picture_1707279371.png"
            )
        )
    }
}