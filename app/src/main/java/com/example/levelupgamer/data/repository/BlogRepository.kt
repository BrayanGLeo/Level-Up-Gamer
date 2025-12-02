package com.example.levelupgamer.data.repository

import com.example.levelupgamer.data.model.Blog
import kotlinx.coroutines.delay

class BlogRepository {

    suspend fun getBlogPosts(): List<Blog> {
        delay(500)
        return getMockedBlogPosts()
    }

    private fun getMockedBlogPosts(): List<Blog> = listOf(
        Blog(
            id = "1",
            title = "PlayStation anuncia los 8 juegos gratis de septiembre de 2025 para miembros de PS Plus Extra y Premium",
            author = "Level-Up Gamer",
            date = "10 Septiembre 2025",
            summary = "El Catálogo de PS Plus Extra y Premium se actualiza el 16 de septiembre con una variedad de géneros: lucha libre, estrategia y supervivencia.",
            content = "El Catálogo de PS Plus Extra y Premium se actualiza el 16 de septiembre con una variedad de géneros: lucha libre, estrategia y supervivencia.",
            image = "https://areajugones.sport.es/wp-content/uploads/2025/08/captura.jpg.webp"
        ),
        Blog(
            id = "2",
            title = "¡Madrid y Barcelona dominan! EA SPORTS FC 26 anuncia los mejores jugadores de LALIGA y Liga F, y hay sorpresas",
            author = "Level-Up Gamer",
            date = "10 Septiembre 2025",
            summary = "La Semana de las Valoraciones de EA SPORTS FC 26 pone el foco en estrellas como Mbappé, Lamine Yamal, Aitana Bonmatí y Alexia Putellas.",
            content = "La Semana de las Valoraciones de EA SPORTS FC 26 pone el foco en estrellas como Mbappé, Lamine Yamal, Aitana Bonmatí y Alexia Putellas.",
            image = "https://i.ytimg.com/vi/lqb0TTKD8jY/maxresdefault.jpg"
        ),
        Blog(
            id = "3",
            title = "NVIDIA RTX Remix recibe un sistema avanzado de partículas con trazado de rayos",
            author = "Level-Up Gamer",
            date = "09 Septiembre de 2025",
            summary = "NVIDIA ha anunciado una nueva actualización de RTX Remix, su plataforma de modding para PC con GPU GeForce RTX.",
            content = "NVIDIA ha anunciado una nueva actualización de RTX Remix, su plataforma de modding para PC con GPU GeForce RTX.",
            image = "https://www.nvidia.com/content/dam/en-zz/Solutions/geforce/ada/rtx-remix/geforce-rtx-remix-portal-rtx-ari.jpg"
        ),
        Blog(
            id = "4",
            title = "Jugadores temen que la próxima consola de Xbox sea demasiado cara por aumentos recientes en el precio de Series X|S",
            author = "Level-Up Gamer",
            date = "20 de Septiembre de 2025",
            summary = "Microsoft subió el precio de Xbox Series X|S una vez más por culpa de los aranceles y demás factores económicos.",
            content = "Microsoft subió el precio de Xbox Series X|S una vez más por culpa de los aranceles y demás factores económicos.",
            image = "https://i0.wp.com/levelup.buscafs.com/2025/09/Precio-Xbox.jpg?fit=1225,610&quality=75&strip=all"
        ),
        Blog(
            id = "5",
            title = "Hay un nuevo shooter táctico gratis petándolo en Steam con un notable y casi 60.000 jugadores",
            author = "Level-Up Gamer",
            date = "22 de Septiembre de 2025",
            summary = "Arena Breakout: Infinite. El título de Morefun Studios, que combina acción táctica y mecánicas de extracción...",
            content = "Arena Breakout: Infinite. El título de Morefun Studios, que combina acción táctica y mecánicas de extracción...",
            image = "https://i.blogs.es/0683a3/maxresdefault-1-_upscayl_2x_remacri-4x-1-/1200_800.jpeg"
        ),
        Blog(
            id = "6",
            title = "PlayStation 5: Potencia de Nueva Generación",
            author = "Level-Up Gamer",
            date = "12 de Octubre de 2024",
            summary = "La PS5 no es solo una consola, es un centro de entretenimiento inmersivo. Con su SSD de ultra alta velocidad, los tiempos de carga son cosa del pasado.",
            content = "La PS5 no es solo una consola, es un centro de entretenimiento inmersivo. Con su SSD de ultra alta velocidad, los tiempos de carga son cosa del pasado.",
            image = "https://static2.pisapapeles.net/uploads/2024/09/GXH7fzoXIAAqLGh.jpeg"
        ),
        Blog(
            id = "7",
            title = "Mando Inalámbrico Xbox: Precisión y Comodidad",
            author = "Level-Up Gamer",
            date = "8 de Octubre de 2024",
            summary = "Microsoft ha perfeccionado un diseño ya legendario. El mando de Xbox Series X mejora el agarre con superficies texturizadas.",
            content = "Microsoft ha perfeccionado un diseño ya legendario. El mando de Xbox Series X mejora el agarre con superficies texturizadas.",
            image = "https://cdn-dynmedia-1.microsoft.com/is/image/microsoftcorp/999644_Hero-0_1920x1080_01:VP2-859x540"
        ),
        Blog(
            id = "8",
            title = "HyperX Cloud II: Escucha Cada Detalle",
            author = "Level-Up Gamer",
            date = "3 de Octubre de 2024",
            summary = "Un clásico por una razón. Los HyperX Cloud II son famosos por su increíble comodidad, permitiendo horas de juego sin fatiga.",
            content = "Un clásico por una razón. Los HyperX Cloud II son famosos por su increíble comodidad, permitiendo horas de juego sin fatiga.",
            image = "https://i.ytimg.com/vi/4lro2eeHwRk/maxresdefault.jpg"
        ),
        Blog(
            id = "9",
            title = "Logitech G502 HERO: El Rey de la Precisión",
            author = "Level-Up Gamer",
            date = "25 de Septiembre de 2024",
            summary = "El G502 HERO es un ícono en el mundo del gaming. Equipado con el sensor HERO 25K para una precisión inigualable.",
            content = "El G502 HERO es un ícono en el mundo del gaming. Equipado con el sensor HERO 25K para una precisión inigualable.",
            image = "https://img.pccomponentes.com/pcblog/1731798000000/logitech-g502-hero-review-3.jpg"
        ),
        Blog(
            id = "10",
            title = "Secretlab TITAN: El Trono Definitivo del Gamer",
            author = "Level-Up Gamer",
            date = "18 de Septiembre de 2024",
            summary = "La Secretlab TITAN Evo es la inversión definitiva en comodidad y ergonomía. Con materiales de primera calidad...",
            content = "La Secretlab TITAN Evo es la inversión definitiva en comodidad y ergonomía. Con materiales de primera calidad...",
            image = "https://images.secretlab.co/subimage/tr:n-w_750_square/M07-E24PU-STELH1R_Hero.jpg"
        )
    )
}