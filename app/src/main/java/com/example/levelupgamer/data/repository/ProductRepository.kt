package com.example.levelupgamer.data.repository

import com.example.levelupgamer.data.local.ProductDao
import com.example.levelupgamer.data.model.Product
import com.example.levelupgamer.data.remote.ApiService

class ProductRepository(
    private val apiService: ApiService,
    private val productDao: ProductDao
) {
    suspend fun getProductsFromApi(): Result<List<Product>> {
        return try {
            val products = apiService.getProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsFromDb(): List<Product> {
        return productDao.getAllProducts()
    }

    suspend fun saveProductsToLocal(products: List<Product>) {
        productDao.clearProducts()
        productDao.insertAll(products)
    }
}