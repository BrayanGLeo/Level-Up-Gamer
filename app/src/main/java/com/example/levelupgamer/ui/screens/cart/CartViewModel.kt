package com.example.levelupgamer.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupgamer.data.model.CartItem
import com.example.levelupgamer.data.model.Order
import com.example.levelupgamer.data.remote.ApiService
import com.example.levelupgamer.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
// ------------------------------------------

data class CartUiState(
    val isLoading: Boolean = false,
    val saleSuccess: Boolean = false,
    val error: String? = null
)

class CartViewModel(
    private val cartRepository: CartRepository,
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubsided(5000),
            initialValue = emptyList()
        )

    val cartTotal: StateFlow<Int> = cartRepository.cartItems
        .map { items -> items.sumOf { it.precio * it.quantity } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubsided(5000),
            initialValue = 0
        )

    fun updateQuantity(codigo: String, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(codigo, newQuantity)
        }
    }

    fun removeFromCart(codigo: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(codigo)
        }
    }

    fun checkout(userEmail: String) {
        viewModelScope.launch {
            _uiState.value = CartUiState(isLoading = true)
            try {
                val items = cartItems.value
                val total = cartTotal.value
                if (items.isEmpty()) {
                    _uiState.value = CartUiState(error = "El carro está vacío")
                    return@launch
                }

                val order = Order(items = items, total = total, customerEmail = userEmail)

                kotlinx.coroutines.delay(1500)
                cartRepository.clearCart()
                _uiState.value = CartUiState(saleSuccess = true)

            } catch (e: Exception) {
                _uiState.value = CartUiState(error = "Error de conexión: ${e.message}")
            }
        }
    }
}