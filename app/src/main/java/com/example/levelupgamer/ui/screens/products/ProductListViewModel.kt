package com.example.levelupgamer.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.model.Product
import com.example.levelupgamer.data.repository.CartRepository
import com.example.levelupgamer.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProductListUiState(
    val isLoading: Boolean = true,
    val error: String? = null
)

class ProductListViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())

    val filteredProducts: StateFlow<List<Product>> = _searchText
        .combine(_products) { text, products ->
            if (text.isBlank()) {
                products
            } else {
                products.filter {
                    it.nombre.contains(text, ignoreCase = true) ||
                            it.descripcion.contains(text, ignoreCase = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadProducts()
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = ProductListUiState(isLoading = true)
            try {
                _products.value = productRepository.getProducts()
                _uiState.value = ProductListUiState(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = ProductListUiState(isLoading = false, error = e.message)
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product)
        }
    }
}