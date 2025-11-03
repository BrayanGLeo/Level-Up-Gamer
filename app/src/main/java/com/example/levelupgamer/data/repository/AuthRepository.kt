package com.example.levelupgamer.data.repository

import com.example.levelupgamer.data.model.User
import kotlinx.coroutines.delay

class AuthRepository {

    suspend fun login(email: String, password: String): Result<User> {
        delay(1000) // Simula espera de red
        if (email == "admin@admin.cl" && password == "admin") {
            return Result.success(
                User(
                    id = "1",
                    email = "admin@admin.cl",
                    name = "Admin",
                    role = "Administrador"
                )
            )
        }
        if (email == "cliente@duoc.cl" && password == "123456") {
            return Result.success(
                User(
                    id = "2",
                    email = "cliente@duoc.cl",
                    name = "Cliente",
                    role = "Cliente"
                )
            )
        }
        return Result.failure(Exception("Correo o contraseña incorrectos."))
    }
}