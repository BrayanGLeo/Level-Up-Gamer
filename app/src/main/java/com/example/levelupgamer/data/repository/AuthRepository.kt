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

    suspend fun register(email: String, name: String, password: String): Result<User> {
        delay(1000)
        if (email == "admin@admin.cl" || email == "cliente@duoc.cl") {
            return Result.failure(Exception("El correo ya está en uso."))
        }

        // Simula un registro exitoso
        return Result.success(
            User(
                id = "3",
                email = email,
                name = name,
                role = "Cliente"
            )
        )
    }
}