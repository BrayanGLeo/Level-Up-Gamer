package com.example.levelupgamer.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.model.Product
import com.example.levelupgamer.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ProductListViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    fun loadFromApi() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.getProductsFromApi()

            result.fold(
                onSuccess = { productsList ->
                    _uiState.update {
                        it.copy(
                            products = productsList,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error de conexión: ${error.message}"
                        )
                    }
                }
            )
        }
    }

    fun loadFromDb() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val localProducts = repository.getProductsFromDb()

            _uiState.update {
                it.copy(
                    products = localProducts,
                    isLoading = false
                )
            }

            if (localProducts.isEmpty()) {
                _uiState.update { it.copy(errorMessage = "La base de datos está vacía. Intenta cargar desde la API y guardar primero.") }
            }
        }
    }

    fun saveToLocalDb() {
        val currentProducts = _uiState.value.products

        if (currentProducts.isNotEmpty()) {
            viewModelScope.launch {
                repository.saveProductsToLocal(currentProducts)
            }
        }
    }
}